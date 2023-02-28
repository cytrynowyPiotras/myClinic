#!/usr/bin/sh

version=$(java -version 2>&1 \
	| head -1 \
	| cut -d'"' -f2 \
	| sed 's/^1\.//' \
	| cut -d'.' -f1
)

if [ $version -ne "19" ]; then 
	echo "Java 19 is required."
	exit 1
fi

java -cp target/clinic_app_new-1.0-SNAPSHOT.jar com.clinicapp.Main

