  # How to use the quickstart backend (Setup github actions and deployment)
* You can etiher watch this video - https://cphbusiness.cloud.panopto.eu/Panopto/Pages/Viewer.aspx?id=99debb59-7f1f-4ee2-b0ec-ada500bb9a88
- or follow these steps: 
  - clone this project in a folder
  - rm -rf .git to remove .git
  - git init
  - git add .
  - git commit -m "first commit"
- before you push
  - add secrets to your github repository on github (name and password that you wrote on your droplet tomcat server)
  - replace all the xxx' places in pom.xml 
  - replace either master or main in mavenworkflow.yml in .github package
  - maven test locally in intelliJ (Should be all green!). 
    - Whener you rest assured test with your own endpoint you can always @Disabled in your rest assured test (above the class name) if it is chaotic and not green. And you dont have much time to deadline (you just have to exaplain why it didn't work, i guess.. in exam)
-  Now push 
-  Now check if your github actions also come up with green sign? (good sign if it is green, success -> deployed on your droplet tomcat server)
-  Write yourDomainName/manager/html in url and check if your java app is actually deployed

### Local work
 - Add your local tomcat server in IDE
 - In Utils Package, run your SetupTestUsers if you want to test locally for the default user and admin IDE

### Added droplet.http in this java app (detailed instructions in the file - MUST READ)
- After PUSH and your java app is succesfully deployed on your droplet tomcat server
- You can now POST newly user with your own name directly to your droplet database in droplet.http file 




