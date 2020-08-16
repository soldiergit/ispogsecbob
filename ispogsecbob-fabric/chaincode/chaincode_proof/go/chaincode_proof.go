/*
Copyright IBM Corp. 2016 All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package main

//WARNING - this chaincode's ID is hard-coded in chaincode_example04 to illustrate one way of
//calling chaincode from a chaincode. If this example is modified, chaincode_example04.go has
//to be modified as well with the new ID of chaincode_example02.
//chaincode_example05 show's how chaincode ID can be passed in as a parameter instead of
//hard-coding.

import (
	"encoding/json"
	"fmt"
	_ "strconv"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

//凭证信息结构体
type Proof struct {
	//时间
	Time string `json:time`
	//凭证文件路近
	FilePath string `json:filepath`
	//文件hash
	HashCode string `json:hashcode`
	//持有者
	Owner string `json:owner`
	//是否过期
	Overdue bool `json:overdue`
}

func (s *Proof) Invoke(APIstub shim.ChaincodeStubInterface) pb.Response {
	// Retrieve the requested Smart Contract function and arguments
	function, args := APIstub.GetFunctionAndParameters()
	// Route to the appropriate handler function to interact with the ledger appropriately
	if function == "verify" {
		return s.Verify(APIstub, args)
	} else if function == "deposit" {
		return s.Deposit(APIstub, args)
	}
	return shim.Error("Invalid Smart Contract function name.")
}

//钩子
func (t *Proof) Init(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Println("初始化链码")
	return shim.Success(nil)
}

//存放凭证
func (p *Proof) Deposit(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	//参数验证
	if len(args) != 5 {
		return shim.Error("Incorrect number of arguments. Expecting 5")
	}
	//构筑结构体
	var proof = Proof{
		Time:     args[0],
		FilePath: args[1],
		HashCode: args[2],
		Owner:    args[3],
		Overdue:  false,
	}
	//序列化
	proofBytes, err := json.Marshal(proof)
	//错误处理
	if err != nil {
		jsonResp := "{\"Error\":\"Failed to marchal: " + args[0] + "\"}"
		return shim.Error(jsonResp)
	}
	//hash作为key
	stub.PutState(args[3], proofBytes)
	//响应
	return shim.Success(proofBytes)
}

//验证
func (p *Proof) Verify(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	//参数验证
	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}
	//查询账本
	proofAsBytes, err := stub.GetState(args[0])

	if err != nil {
		jsonResp := "{\"Error\":\"Failed to get state for: " + args[0] + "\"}"
		return shim.Error(jsonResp)
	}
	//返回结果
	//查询不到则返回空
	return shim.Success(proofAsBytes)
}

func main() {
	err := shim.Start(new(Proof))
	if err != nil {
		fmt.Printf("Error starting Proof chaincode: %s", err)
	}
}
