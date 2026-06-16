#!/bin/bash

# 1. Force l'utilisation de Java 17 (Trés important pour Tomcat 10)
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64

# 2. Nettoyage complet et frais de ton dossier de compilation local
rm -rf bin
mkdir -p bin

# 3. Compilation de ton FrontControllerServlet
javac -cp "lib/servlet-api.jar" -d bin src/main/java/FrontControllerServlet.java

# 4. Création du fichier JAR du framework
jar -cvf essai.jar -C bin .

# 5. AUTOMATISATION : Envoi direct du JAR dans le dossier lib du testFramework
# Cela va écraser l'ancien JAR automatiquement sans intervention de ta part
TARGET_LIB="/home/itu/tomcat/webapps/testFramework/WEB-INF/lib"
mkdir -p "$TARGET_LIB"
cp -f essai.jar "$TARGET_LIB/"

# 6. Nettoyage du cache de Tomcat (Évite que le serveur garde l'ancienne version en mémoire)
rm -rf /home/itu/tomcat/work/Catalina/localhost/testFramework

# 7. Redémarrage automatique de Tomcat pour appliquer les changements
cd /home/itu/tomcat/bin
./shutdown.sh
./startup.sh

echo "--------------------------------------------------------"
echo "🚀 Framework mis à jour, déployé et Tomcat redémarré !"
echo "--------------------------------------------------------"