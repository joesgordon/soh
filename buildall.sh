#!/bin/bash

set -e

apps_dir=~/Desktop/apps/

mkdir -p ${apps_dir}

#------------------------------------------------------------------------------

pushd krelay/armv7/
./buildit.sh
popd
cp krelay/armv7/krelay.exe krelay/armv7/fixftdi.sh ${apps_dir}
chmod a+ ${apps_dir}/fixftdi.sh

#------------------------------------------------------------------------------

pushd jutils/JUtils
ant
git checkout src/org/jutils/icons/info.properties
popd

#------------------------------------------------------------------------------

pushd scioly
ant
popd

cp scioly/build/scioly.jar ${apps_dir}

#------------------------------------------------------------------------------

pushd boomilever
ant
popd

cp boomilever/build/boomilever.jar boomilever/boomilever.sh ${apps_dir}
chmod a+x ${apps_dir}/boomilever.sh

#------------------------------------------------------------------------------

pushd gravityvehicle
ant
popd

cp gravityvehicle/build/gravityvehicle.jar gravityvehicle/gravityvehicle.sh ${apps_dir}
chmod a+x ${apps_dir}/gravityvehicle.sh

#------------------------------------------------------------------------------

pushd pingpongparachute
ant
popd

cp pingpongparachute/build/pingpongparachute.jar pingpongparachute/pingpongparachute.sh ${apps_dir}
chmod a+x ${apps_dir}/pingpongparachute.sh

#------------------------------------------------------------------------------


