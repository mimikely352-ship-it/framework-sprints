creer classe annotation 1 : @Controller (dans framework)

(dans test): TestController annote par @Controller

### Sprint 1:
But: fatatritsika oe iza daoly le Controller

code: - executer au demarrage de l'appli web (Listener ou init) 
      - au 1e appel du Frontservlet (methode init)
      
      Frontservlet: un seul attribut: list string listcontroller
      init():parcourir toutes les classes dans le classpath: il y a de l'annotation? si oui: ajouter dans la liste


annotation.Controller: soit tous, soit liste package

web.xml de test: creer une variable ayant la valeur de l'annotation controller du framework

dans classe Utilitaire: methode qui verifie si l'annotation existe ou pas: package, annotation,au niveau quoi(mehode,classe,...)

--------------------------------------------------------------------------------------------------------------------------

### Sprint 2:
But: rehefa ity url ity no antsoina de ity methode ity no antsoina (sans executer pour l'instant)
ex: @Controller
    EmpController 

    @UrlMapping("/emp/list")
    liste(){

    }

cree annotation methode mila variable 
liste de url: Classe-> methode
de la forme:ex: /emp/list     EmpController-> liste

                /emp/new      EmpController-> create 

refa tsy fatatra le url de mthrows exception de tenenina oe reto ny url misy
si il choisit un url de mnethode et classe ny iany no afficheny: EmpController-> create
NB: assurer l'unicite des url 


### Sprint 3:
    meme url mais l'un peut etre POST et l'autre GET
    ex: @urlmapping("/test) - GET
        @urlmapping("/test) - POST

        creer classe URLMethod : url,methode GET/POST
        amboarina le map map(URLMethod, Mapping)
        equals+hashcode
test: methode 2mitovy + mitovy url => exception

### Sprint 3bis:
    refa fatatra le methode de executena (invoke) sans creer l'instance (ao anaty processRequest)