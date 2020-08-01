#!/usr/bin/env bash
echo "[install chain-code]"
docker exec cli peer chaincode install -n proof_chaincode -v 1.0 -l golang -p github.com/chaincode/chaincode_proof/go/
docker exec cli2 peer chaincode install -n proof_chaincode -v 1.0 -l golang -p github.com/chaincode/chaincode_proof/go/
docker exec cli3 peer chaincode install -n proof_chaincode -v 1.0 -l golang -p github.com/chaincode/chaincode_proof/go/
echo "[instantiate chain-code]"
docker exec cli peer chaincode instantiate -o orderer.example.com:7050 --tls true --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem -C mychannel -n proof_chaincode -l golang -v 1.0 -c '{"Args":["init","a","100","b","200"]}' -P 'AND ('\''Org1MSP.peer'\'','\''Org2MSP.peer'\'','\''Org3MSP.peer'\'')'
sleep 1
echo "[sleep 20 second wait chain-code instantiate]"
sleep 19
echo "[query]"
docker exec cli peer chaincode query -C mychannel -n proof_chaincode -c '{"Args":["verify","hashcode"]}'
echo "[deposit]"
docker exec cli peer chaincode invoke -o orderer.example.com:7050 --tls true --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem -C mychannel -n proof_chaincode --peerAddresses peer0.org1.example.com:7051 --tlsRootCertFiles /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt --peerAddresses peer0.org2.example.com:9051 --tlsRootCertFiles /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt --peerAddresses peer0.org3.example.com:11051 --tlsRootCertFiles /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org3.example.com/peers/peer0.org3.example.com/tls/ca.crt -c '{"Args":["deposit","2020-03-04","filepath","hashcode","owner","overdue"]}'
echo "[query]"
docker exec cli peer chaincode query -C mychannel -n proof_chaincode -c '{"Args":["verify","hashcode"]}'