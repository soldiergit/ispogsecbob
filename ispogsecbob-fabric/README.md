# Fabric Application

## 构建网络

1.下载相关镜像及其二进制文件

`curl -sSL http://bit.ly/2ysbOFE | bash -s -- 1.4.8 1.4.8 0.4.21`
curl -sSL http://bit.ly/2ysbOFE | bash -s -- 1.4.2 1.4.2 0.4.15

或者

`./bootstrap.sh 1.4.8 1.4.8 0.4.21`

2.配置环境变量

```

```

3.启动网络

 `y|./byfn.sh up -o etcdraft -s couchdb`  

## 启动服务器

1.启动服务器

````
cd application

go run client/main.go
````