import com.nirima.jenkins.plugins.docker.DockerCloud
import com.nirima.jenkins.plugins.docker.DockerTemplate
import com.nirima.jenkins.plugins.docker.DockerTemplateBase
import com.nirima.jenkins.plugins.docker.DockerImagePullStrategy
import com.nirima.jenkins.plugins.docker.launcher.AttachedDockerComputerLauncher
import io.jenkins.docker.connector.DockerComputerAttachConnector
import jenkins.model.Jenkins

def image = System.getenv("DOCKER_PLUGIN_IMAGE")
def volumesString = System.getenv("DOCKER_PLUGIN_VOLUMESSTRING")
def labelString = System.getenv("DOCKER_PLUGIN_LABELSTRING")

// parameters
def dockerTemplateBaseParameters = [
  image:              image,
  pullCredentialsId:  '',
  dnsString:          '',
  network:            '',
  dockerCommand:      '',
  volumesString:      volumesString,
  volumesFromString:  '',
  environmentsString: '',
  hostname:           '',
  user:               '',
  extraGroupsString:  '',
  memoryLimit:        1000,
  memorySwap:         null,
  cpuShares:          null,
  shmSize:            null,
  bindPorts:          '',
  bindAllPorts:       false,
  privileged:         false,
  tty:                true,
  macAddress:         '',
  extraHostsString:   ''
]

def DockerTemplateParameters = [
  instanceCapStr: '4',
  labelString:    labelString,
  remoteFs:       ''
]

def dockerCloudParameters = [
  connectTimeout:   3,
  containerCapStr:  '4',
  credentialsId:    '',
  dockerHostname:   '',
  name:             'docker.local',
  readTimeout:      60,
  serverUrl:        'unix:///var/run/docker.sock',
  version:          ''
]

// https://github.com/jenkinsci/docker-plugin/blob/docker-plugin-1.2.0/src/main/java/com/nirima/jenkins/plugins/docker/DockerTemplateBase.java
DockerTemplateBase dockerTemplateBase = new DockerTemplateBase(
  dockerTemplateBaseParameters.image,
  dockerTemplateBaseParameters.pullCredentialsId,
  dockerTemplateBaseParameters.dnsString,
  dockerTemplateBaseParameters.network,
  dockerTemplateBaseParameters.dockerCommand,
  dockerTemplateBaseParameters.volumesString,
  dockerTemplateBaseParameters.volumesFromString,
  dockerTemplateBaseParameters.environmentsString,
  dockerTemplateBaseParameters.hostname,
  dockerTemplateBaseParameters.user,
  dockerTemplateBaseParameters.extraGroupsString,
  dockerTemplateBaseParameters.memoryLimit,
  dockerTemplateBaseParameters.memorySwap,
  dockerTemplateBaseParameters.cpuShares,
  dockerTemplateBaseParameters.shmSize,
  dockerTemplateBaseParameters.bindPorts,
  dockerTemplateBaseParameters.bindAllPorts,
  dockerTemplateBaseParameters.privileged,
  dockerTemplateBaseParameters.tty,
  dockerTemplateBaseParameters.macAddress,
  dockerTemplateBaseParameters.extraHostsString
)

// https://github.com/jenkinsci/docker-plugin/blob/docker-plugin-1.2.0/src/main/java/com/nirima/jenkins/plugins/docker/DockerTemplate.java
DockerTemplate dockerTemplate = new DockerTemplate(
  dockerTemplateBase,
  new DockerComputerAttachConnector(),
  DockerTemplateParameters.labelString,
  DockerTemplateParameters.remoteFs,
  DockerTemplateParameters.instanceCapStr
)

// https://github.com/jenkinsci/docker-plugin/blob/docker-plugin-1.2.0/src/main/java/com/nirima/jenkins/plugins/docker/DockerCloud.java
DockerCloud dockerCloud = new DockerCloud(
  dockerCloudParameters.name,
  [dockerTemplate],
  dockerCloudParameters.serverUrl,
  dockerCloudParameters.containerCapStr,
  dockerCloudParameters.connectTimeout,
  dockerCloudParameters.readTimeout,
  dockerCloudParameters.credentialsId,
  dockerCloudParameters.version,
  dockerCloudParameters.dockerHostname,
)

// get Jenkins instance
Jenkins jenkins = Jenkins.getInstance()

// add cloud configuration to Jenkins
jenkins.clouds.add(dockerCloud)

// save current Jenkins state to disk
jenkins.save()
