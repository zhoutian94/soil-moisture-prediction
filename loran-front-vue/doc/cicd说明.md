### 使用说明
1·首先将deploy.sh放入需要部署应用的服务器的某个位置，并赋予脚本执行权限
2·将jenkinsfile中对应的环境变量设置为该服务器的信息：包括服务器的ip、脚本所在目录（TARGET_DIR）、脚本名称（REMOTE_SCRIPT）等
3·在coding平台使用jenkinsfile作为构建计划即可开始构建。