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

--------------------------------------------------------------------------------------------------------------------------

### Sprint 3:
    meme url mais l'un peut etre POST et l'autre GET
    ex: @urlmapping("/test) - GET
        @urlmapping("/test) - POST

        creer classe URLMethod : url,methode GET/POST
        amboarina le map map(URLMethod, Mapping)
        equals+hashcode
test: methode 2mitovy + mitovy url => exception

--------------------------------------------------------------------------------------------------------------------------

### Sprint 3bis:
    refa fatatra le methode de executena (invoke) sans creer l'instance (ao anaty processRequest)

--------------------------------------------------------------------------------------------------------------------------

### Sprint 5: (resaka andefa donnee makany am vue)
    - creer classe Modelandview: map(string,object),view  (OK)  
    web.mxl: preffixe, suffixe  (OK)
    suffixe: .jsp  (OK)
    prefixe: webapp,web-inf/jsp,...(OK)
    invoke les methodes, recuperer les valeurs de retour ppour savoir quel vue on utilise-> instance modelandview,
                        recuperer les arguments
                        recuperer le url et concatener avec le sufixe et prefixe

    ex: ModelAndview liste()....

--------------------------------------------------------------------------------------------------------------------------

### Sprint 5bis: 
declarwer listner spring dans web.xml du projet de test => mdemarrer le contener [OK]
applicationContext(ato no misy ny bean retra2)
ao am methode de test:
ex: lister(applicatiocpontext cx){
    cx.getbean()
}
- dans frontcontrollerlistner :
    - ajout d'un variable static final SPRING_ROOT
    - mettre valeur de SPRING_ROOT à "org.springframework.web.context.WebApplicationContext.ROOT"
    - envoyer un attribut nommer springcontext qui contient la valeur de SPRING_ROOT dans le context :
        servletContext.setAttribute("springContext", servletContext.getAttribute(SPRING_ROOT))
- creation d'un classe Util :
    - ajout de la fonction ststic boolean haveParameter(Method methode, Class<?> param) qui verifie si une methode à la classe param comme parametre
- dans frontcontroller :
    - recuperer le springcontext depuis le context et caster en WebApplicationContext
    - changer l'invocation de la methode :
        - verifier si la methode attend une parmetre WebApplication avec la fonction haveParam()
            - si oui : 
                - si springcontext == null : throw excepltion pas de springcontext
                - invoker la methode en mettant en argument le springcontext :
                    - result = (ModelAndView) method.invoke(obj,  springContext);
            - sinon : invocation simple comme avant

