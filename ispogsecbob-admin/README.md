# 基于区块链政校企合作信息共享平台(ISPOGSECBOB)

#### 项目介绍

基于区块链政校企合作信息共享平台

Information sharing platform of government school enterprise cooperation based on blockchain

#### 软件架构

软件架构说明  

技术栈：

>spring boot，spring mvc，mybatis，mybatis plus，hyperledger fabric


#### 安装教程

1. idea自带数据库管理工具导入方式 创建ispogsecbob数据库命令：

```sql
CREATE SCHEMA ispogsecbob DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
```
2. maven引入jar包

>选择maven插件中的install 或 `mvn install`

3. 运行ISPOGSECBOBApplication

#### 使用说明

1. 构建镜像： `mvn clean package docker:build`
2. 部署应用： `docker run --name ispogsecbob --privileged=true -d -p 8080:8080 -v /root/mikey/MIKEY:/home/mikey/MIKEY 90fbb84d9eb1`
3. 下载依赖： `mvn clean install`
4. 构建镜像： `docker build -t mikeyboom/ispogsecbob:lastst .`
5. 推到仓库： `docker push mikeyboom/ispogsecbob`
6. 拉取镜像： `docker pull mikeyboom/ispogsecbob:latest`
6. 启动服务： `docker-compose -f docker-compose.yml -d`


#### 持续集成平台

>Travis CI

#### 

