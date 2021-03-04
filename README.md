# Meteo_App
/****************************************************Introduction générale********************************************************************************************/

L’application : Notre application fait l’étude des températures observées sur toute la planète depuis une centaine d’années à peu près.
L’idée est de pouvoir observer les variations de température en fonction de plusieurs critères et de pouvoir afficher ça, simplement en demande.
L’utilisateur peut alors entrer des paramètres, quelle observation il veut regarder et avoir en affichage un tableau contenant les informations demandées.

/***********************************************************Analyse du besoin*****************************************************************************************/

L’objectif de ce projet ne se résume pas à faire une consultation des données historiques ou massives mais plutôt de fournir à l’utilisateur final un engin capable 
de traiter et agréger des données volumineuses provenant du site web du gouvernement américain du National Oceanic and Atmospheric Administration « ftp.ncdc.noaa.gov ».

/********************************************************Vue fonctionnelle du système*********************************************************************************/

La première étape consiste à filtrer les données récupérer depuis internet.

• Importer les fichiers télécharger dans L’HDFS, dans le Système de Fichiers distribués d’Hadoop et En utilisant un Job MapReduce nous allons effectuer un bulkload afin 
d’insérer les données dans la base de données Nosql HBase en utilisant la famille de colonne SLocation pour le fichier contenant les informations de la localisation 
des stations et la famille de colonne STemperature pour le fichier contenant les données des températures.

• En utilisant un deuxième Job MapReduce nous allons effectuer un une agrégation des données dans la base de données Nosql HBase en utilisant la famille de colonne
SLocation et la famille de colonne STemperature comme éléments d’entrer pour générer une nouvelle famille de colonne SAgregation qui va contenir les informations
des températures agrégées en mois et année. 

• En utilisant un Troisième Job MapReduce nous allons charger les données agrégées auparavant de puis la famille de colonne SAgregation afin de les exporter vers la base
de données SQL MySQL.

• Le dernier Job est une agrégation depuis la base de données MySQL et précisément la table contenant les agrégations mensuelles vers la table d’agrégation annuelle.

• à La fin du chargement des données et après agrégation et dans le cas où tous les programmes s’exécutent correctement l’utilisateur sera redirigé vers une page de
consultation des données.

/********************************************************Environnement technique*************************************************************************************/

Serveur AIX ou UNIX qui dispose de la configuration suivante :

==> Système d’exploitation : Linux CentOS 7.

==> 16 Go de RAM.

Les Technologies utilisèes dans ce projet sont : 

==> Spring Framework.

==> Apache Hadoop.

==> HBase.

==> MySQL.

==> Apache HTTP Server.

==> HTML5.

==> Bootstrap.

Merci de consulter le repertoire suivant pour la configuration de l'environnement : /%%%%%%%%%%%%%%%%%%% ZLOUAK/Environnment_Setting %%%%%%%%%%%%%%%%%%/

Après installations et demarrage des services : 

/*==============================================>Java<=======================================================*/
--pour 64 bits

#wget https://github.com/frekele/oracle-java/releases/download/8u212-b10/jdk-8u212-linux-x64.tar.gz     

#tar zxvf jdk-8u212-linux-x64.tar.gz  ou #tar zxvf jdk-8u212-linux-i586.tar.gz

#su

#mv jdk1.8.0_212  /usr/java   --attention au nom du repertoire après décompression
/*==============================================>Haddop<=======================================================*/

==>Ajouter groupe hadoop et user hduser

#groupadd hadoop

#adduser  -G hadoop hduser

#passwd hduser

#su - hduser

--Configuration ssh pour activer un login sans mot de pass

#ssh-keygen -t rsa -P ""

#cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys

#chmod 0600 ~/.ssh/authorized_keys

--verification

#ssh localhost

#exit

--Télécharger hadoop

#wget http://www.eu.apache.org/dist/hadoop/common/hadoop-2.x.y/hadoop-2.x.y.tar.gz

par exemple 

#wget http://www.eu.apache.org/dist/hadoop/common/hadoop-2.9.2/hadoop-2.9.2.tar.gz

#tar xzf hadoop-2.x.y.tar.gz

#mv hadoop-2.x.y /opt/hadoop

--Définir Variables d'environement ~/.bashrc ou ~/.bash_profile

export JAVA_HOME=/usr/java/

export PATH=$PATH:$JAVA_HOME/bin

export HADOOP_HOME=/opt/hadoop/

export HADOOP_INSTALL=$HADOOP_HOME

export HADOOP_MAPRED_HOME=$HADOOP_HOME

export HADOOP_COMMON_HOME=$HADOOP_HOME

export HADOOP_HDFS_HOME=$HADOOP_HOME

export YARN_HOME=$HADOOP_HOME

export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_HOME/lib/native

export HADOOP_OPTS="-Djava.library.path=$HADOOP_INSTALL/lib/native"

export HADOOP_CLASSPATH=$(hadoop classpath)

export HADOOP_CLASSPATH=$JAVA_HOME/jre/lib:$JAVA_HOME/lib:$JAVA_HOME/lib/tools.jar

export PATH=$PATH:$HADOOP_HOME/sbin:$HADOOP_HOME/bin

--Appliquer la définition des variables

#source ~/.bashrc    ou bien  #source ~/bash_profile

--editer le fichier $HADOOP_HOME/etc/hadoop/hadoop-env.sh  et définir la variable JAVA_HOME. Changer le path de java selon son installation sur le système.

export JAVA_HOME=/usr/java/

#sudo mkdir -p /opt/hadoop/hadoopdata/namenode

#sudo mkdir -p /opt/hadoop/hadoopdata/datanode

#sudo chown -R hduser:hadoop  /opt/hadoop

--Editer les fichiers de configuration

$ cd $HADOOP_HOME/etc/hadoop


Edit core-site.xml_____________________________________________________________________

	<configuration>
	<property>
	  <name>fs.default.name</name>
	    <value>hdfs://localhost:9000</value>
	</property>
	</configuration>	
Edit core-site.xml_____________________________________________________________________


Edit hdfs-site.xml_____________________________________________________________________

	<configuration>
	<property>
	 <name>dfs.replication</name>
	 <value>1</value>
	</property>

	<property>
	  <name>dfs.name.dir</name>
	    <value>file:/opt/hadoop/hadoopdata/namenode</value>
	</property>

	<property>
	  <name>dfs.data.dir</name>
	    <value>file:/opt/hadoop/hadoopdata/datanode</value>
	</property>
	</configuration>
Edit hdfs-site.xml_____________________________________________________________________________


#mv mapred-site.xml.template mapred-site.xml
Edit mapred-site.xml___________________________________________________________________________

	<configuration>
	 <property>
	  <name>mapreduce.framework.name</name>
	   <value>yarn</value>
	 </property>
	</configuration>
Edit mapred-site.xml___________________________________________________________________________


Edit yarn-site.xml_____________________________________________________________________________

	<configuration>
	 <property>
	  <name>yarn.nodemanager.aux-services</name>
	    <value>mapreduce_shuffle</value>
	 </property>
	</configuration>
Edit yarn-site.xml_____________________________________________________________________________

Formatage du Namenode

#hdfs namenode -format

Démmarage des services hdfs et yarn

#start-dfs.sh

#start-yarn.sh

--Vérification

#jps
    
    --http://localhost:50070
    
    --http://localhost:50095
    
    --http://localhost:8088
    
    --http://localhost:8042

/*==============================================>HBase<=======================================================*/

--Télécharger HBASE

#wget http://apache.mirror.gtcomm.net/hbase/stable/hbase-1.4.9-bin.tar.gz

#tar -zxvf hbase-1.4.9-bin.tar.gz

#sudo mv hbase-1.4.9 /opt/Hbase

--Ajouter à .bashrc

export HBASE_HOME=/opt/Hbase

export HBASE_CONF_DIR=$HBASE_HOME/conf

export HBASE_CLASS_PATH=$HBASE_CONF_DIR

export HBASE_CLASSPATH=$HBASE_HOME/lib

export CLASSPATH=$HADOOP_CLASSPATH:$HBASE_CLASSPATH:/home/bd2c/HbaseProjectJava

export PATH=$PATH:$HBASE_HOME/bin

#source .bashrc

#sudo cd /opt/Hbase/conf

--ouvrir le fichier hbase-env.sh et ajouter

export JAVA_HOME=/usr/java


--ouvrir hbase-site.xml et ajouter le texte

Edit hbase-site.xml_______________________________________________________________________________

        <configuration>
         <property>
                <name>hbase.rootdir</name>
                <value>hdfs://localhost:9000/hbase</value>
         </property>
	
         <property>
                <name>hbase.zookeeper.property.dataDir</name>
                <value>/home/bd2c/zookeeper</value>
         </property>
   
         <property>
                <name>hbase.cluster.distributed</name>
                <value>true</value>
         </property>
        </configuration>
Edit hbase-site.xml________________________________________________________________________________

--s'assurer que le dossier de zookeeper est crée

#sudo mkdir -p /home/bd2c/zookeeper

#sudo chown -R bd2c:hadoop /home/bd2c

--Hbase crée son propre répertoire sur HDFS à vérifier par

#hdfs dfs -ls /hbase

--interface web 

http://localhost:60010/master-status  HMaster

http://localhost:60030                HRegioServer

-- Finalement verifier par jps que les servises Hadoop sont démarrés

-- Démarrer Hbase par

#start-hbase.sh

--Lancer hbase shell

#start-hbase.sh

-- vous devez avoir donc comme prompt

hbase(main):0001:0>

--Commandes de base

Tapez ces commandes :

– exit

/*=============================================>MySQL<=======================================================*/

Telecharger

#wget https://dev.mysql.com/get/mysql57-community-release-el7-9.noarch.rpm

#sudo rpm -ivh mysql57-community-release-el7-9.noarch.rpm

#sudo yum install mysql-server

Demarrer  MySQL services 

#sudo systemctl start mysqld

Verifier MySQL services 

#sudo systemctl status mysqld

/********************************************************Références*************************************************************************************/

Références des données.

==> Informations relatives aux stations :

	ID-Station.
	Latitude (in decimal degrees)
	Longitude (in decimal degrees)
	Élévation (en mètre, valeur manquante = -999.9)
	Non de la station.

==> Informations relatives aux températures :
  
  Agrégations
    	
	AVG : Moyenne des températures.
  	SUM : Somme des températures.
    	Min : La plus petite des valeurs min sur une période précise.
    	Max : La plus grande des valeurs max sur une période précise.
    	Range : La différence entre le Max et le Min sur une période précise.
  
  Description des champs :
    	
	PRCP = Precipitation (dixième de mm).
    	SNOW = Snowfall (mm).
    	SNWD = Snow depth (mm)
    	TMAX = Maximum temperature (dixième de degrés Celsius).
    	TMIN = Minimum temperature (dixième de degrés Celsius).
   	TOBS = Temperature at the time of observation (dixième de degrés Celsius)
