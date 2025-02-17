#!/bin/bash
set -e

# start  by experimenting local run.
# note about space consumption: out-of-space testing on cloud shell instance.

# poc with one specified repo - vision
#cmd line:: ./generate-one.sh -c vision -v 3.1.2 -i google-cloud-vision -g com.google.cloud -p 3.5.0-SNAPSHOT -d 1

# by default, do not download repos
download_repos=0
while getopts c:v:i:g:d:p:f:x:z:m: flag
do
    case "${flag}" in
        c) client_lib_name=${OPTARG};;
        i) client_lib_artifactid=${OPTARG};;
        g) client_lib_groupid=${OPTARG};;
        p) parent_version=${OPTARG};;
        x) googleapis_commitish=${OPTARG};;
        z) monorepo_commitish=${OPTARG};;
        f) googleapis_folder=${OPTARG};;
        m) monorepo_folder=${OPTARG};;
        d) download_repos=1;;
    esac
done
echo "Client Library Name: $client_lib_name";
echo "Client Library GroupId: $client_lib_groupid";
echo "Client Library ArtifactId: $client_lib_artifactid";
echo "Parent Pom Version: $parent_version";
echo "Googleapis Folder: $googleapis_folder";
echo "Googleapis Commitish: $googleapis_commitish";
echo "Monorepo commitish: $monorepo_commitish";
echo "Monorepo folder: $monorepo_folder";

starter_artifactid="$client_lib_artifactid-spring-starter"

# setup git

## install bazel - commented out as running on local already with bazel
#sudo apt-get update
#sudo apt-get install bazel

WORKING_DIR=`pwd`


if [[ $download_repos -eq 1 ]]; then
  bash download-repos.sh
fi

bash setup-googleapis-rules.sh -x $googleapis_commitish

cd googleapis

## If $googleapis_folder does not exist, exit
if [ ! -d "$googleapis_folder" ]
then
  echo "Directory $googleapis_folder DOES NOT exists."
  exit
fi
# Modify BUILD.bazel file for library
# Additional rule to load
SPRING_RULE_NAME="    \\\"java_gapic_spring_library\\\","
perl -0777 -pi -e "s/(load\((.*?)\"java_gapic_library\",)/\$1\n$SPRING_RULE_NAME/s" $googleapis_folder/BUILD.bazel
# Duplicate java_gapic_library rule definition
perl -0777 -pi -e "s/(java_gapic_library\((.*?)\))/\$1\n\n\$1/s" $googleapis_folder/BUILD.bazel
# Update rule name to java_gapic_spring_library
perl -0777 -pi -e "s/(java_gapic_library\()/java_gapic_spring_library\(/s" $googleapis_folder/BUILD.bazel
# Update name argument to have _spring appended
perl -0777 -pi -e "s/(java_gapic_spring_library\((.*?)name = \"(.*?)\")/java_gapic_spring_library\(\$2name = \"\$3_spring\"/s" $googleapis_folder/BUILD.bazel
# todo: better way to remove the following unused arguments?
perl -0777 -pi -e "s/(java_gapic_spring_library\((.*?)(\n    test_deps = \[(.*?)\](.*?),))/java_gapic_spring_library\(\$2/s" $googleapis_folder/BUILD.bazel
perl -0777 -pi -e "s/(java_gapic_spring_library\((.*?)(\n    deps = \[(.*?)\](.*?),))/java_gapic_spring_library\(\$2/s" $googleapis_folder/BUILD.bazel
perl -0777 -pi -e "s/(java_gapic_spring_library\((.*?)(\n    rest_numeric_enums = (.*?),))/java_gapic_spring_library\(\$2/s" $googleapis_folder/BUILD.bazel

# sometimes the rule name doesnt have the same prefix as $client_lib_name
# we use this perl command capture the correct prefix
rule_prefix=$(cat $googleapis_folder/BUILD.bazel | grep _java_gapic_spring | perl -lane 'print m/"(.*)_java_gapic_spring/')

echo "CALL BAZEL TARGET"
# call bazel target
bazelisk build //$googleapis_folder:"$rule_prefix"_java_gapic_spring || exit

cd -

## copy spring code to outside
mkdir -p ../spring-cloud-previews
cp googleapis/bazel-bin/$googleapis_folder/"$rule_prefix"_java_gapic_spring-spring.srcjar ../spring-cloud-previews

# unzip spring code
cd ../spring-cloud-previews
unzip -o "$rule_prefix"_java_gapic_spring-spring.srcjar -d "$starter_artifactid"/
rm -rf "$rule_prefix"_java_gapic_spring-spring.srcjar

# override versions & names in pom.xml

sed -i 's/{{client-library-group-id}}/'"$client_lib_groupid"'/' "$starter_artifactid"/pom.xml
sed -i 's/{{client-library-artifact-id}}/'"$client_lib_artifactid"'/' "$starter_artifactid"/pom.xml
sed -i 's/{{parent-version}}/'"$parent_version"'/' "$starter_artifactid"/pom.xml


# add module after line with pattern, check for existence. Also add readme line if $3 is set to 1.
# args: 1 -  path-to-pom-file; 2 - string-pattern; 3 - 1 if need to generate readme line
add_module_to_pom () {
  xmllint --debug --nsclean --xpath  "//*[local-name()='module']/text()" $1 \
    | sort | uniq | grep -q $starter_artifactid || module_list_is_empty=1
  found_library_in_pom=$?
  if [[ found_library_in_pom -eq 0 ]] && [[ $module_list_is_empty -ne 1 ]]; then
    echo "module $starter_artifactid already found in $1 modules"
  else
    echo "adding module $starter_artifactid to pom"
    sed -i "/$2/a\ \ \ \ <module>"$starter_artifactid"</module>" $1
    if [[ ${3:-1} -eq 1 ]]; then
      # also write to spring-cloud-previews/README.md
      # format |name|distribution name|
      echo -e "|[$monorepo_folder](https://github.com/googleapis/google-cloud-java/blob/$monorepo_commitish/$monorepo_folder/README.md)|com.google.cloud:$starter_artifactid|" >> README.md
      {(grep -vw ".*:.*" README.md);(grep ".*:.*" README.md| sort | uniq)} > tmpfile && mv tmpfile README.md
    fi
  fi
}

add_module_to_pom pom.xml "^[[:space:]]*<modules>" 1

# remove downloaded repos
cd ../spring-cloud-generator
if [[ $download_repos -eq 1 ]]; then
  rm -rf googleapis
  rm -rf gapic-generator-java
fi
