package main

import (
	_ "fabric-application/application/client/routers"
	"github.com/astaxie/beego"
)

func main() {
	beego.Run()
}
