#!/bin/bash

export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64

rm -rf bin
mkdir -p bin

echo "--- Étape 1 : Recherche de TOUS les fichiers Java ---"
find src/main -name "*.java" > framework_sources.txt

echo "Fichiers trouvés :"
cat framework_sources.txt

# Compilation
javac -cp "lib/*" -d bin @framework_sources.txt
rm framework_sources.txt

echo "--- Étape 2 : Création du JAR ---"
cd bin
jar -cvf ../essai.jar *
cd ..

echo "--- Étape 3 : Copie vers testFramework ---"
cp -f essai.jar /opt/lampp/htdocs/testFramework/lib/

echo "--------------------------------------------------------"
echo "🚀 Framework recompilé avec TOUTES les classes !"
echo "--------------------------------------------------------"