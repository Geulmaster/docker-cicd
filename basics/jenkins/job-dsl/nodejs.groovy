job('NodeJS example') {
    scm {
        git('https://github.com/Geulmaster/docker-cicd.git') {  node -> // is hudson.plugins.git.GitSCM
            node / gitConfigName('Geulmaster')
            node / gitConfigEmail('eyal.geulayev@gmail.com')
        }
    }
    triggers {
        scm('H/5 * * * *')
    }
    wrappers {
        nodejs('nodejs') // this is the name of the NodeJS installation in 
                         // Manage Jenkins -> Configure Tools -> NodeJS Installations -> Name
    }
    steps {
        shell("npm install")
    }
}

job('NodeJS Docker example') {
    scm {
        git('https://github.com/Geulmaster/docker-cicd.git') {  node -> // is hudson.plugins.git.GitSCM
            node / gitConfigName('Geulmaster')
            node / gitConfigEmail('eyal.geulayev@gmail.com')
        }
    }
    triggers {
        scm('H/5 * * * *')
    }
    wrappers {
        nodejs('nodejs') 
    }
    steps {
        dockerBuildAndPublish {
            repositoryName('geulmaster/docker-cicd') //qa / dev
            tag('${GIT_REVISION,length=9}')
            registryCredentials('dockerhub')
            buildContext('./basics')
            forcePull(false)
            forceTag(false)
            createFingerprints(false)
            skipDecorate()
        }
    }

pipelineJob('DSL_Pipeline') {

  def repo = 'https://github.com/Geulmaster/docker-cicd.git'

  triggers {
    scm('H/5 * * * *')
  }
  description("Pipeline for $repo")

  definition {
    cpsScm {
      scm {
        git {
          remote { url(repo) }
          branches('master')
          scriptPath('./basics/misc/Jenkinsfile')
          extensions { }  // required as otherwise it may try to tag the repo, which you may not want
        }
      }
    }
  }
 }