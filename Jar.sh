#!/usr/bin/env bash

javac -d . centro/*.java centro/connection/*.java centro/interfaz/*.java centro/document/*.java

rm -rf Centro.jar

mkdir META-INF
echo Manifest-Version: 1.0 > META-INF/MANIFEST.MF
echo Main-Class: centro.Inicio >> META-INF/MANIFEST.MF

zip -r Centro.jar Imagen.png bbdd/* centro/*.class centro/connection/*.class centro/interfaz/*.class centro/document/*.class com net org repackage schemaorg_apache_xmlbeans schemasMicrosoftComOfficeExcel schemasMicrosoftComOfficeOffice schemasMicrosoftComVml META-INF > /dev/null

rm -rf META-INF centro/*.class centro/connection/*.class centro/interfaz/*.class centro/document/*.class
