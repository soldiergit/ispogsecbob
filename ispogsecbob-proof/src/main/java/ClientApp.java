/*
SPDX-License-Identifier: Apache-2.0
*/

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;

import org.hyperledger.fabric.gateway.*;

public class ClientApp {

	static {
		System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
	}

//	public static void main(String[] args) throws Exception {
//		// Load a file system based wallet for managing identities.
//		Path walletPath = Paths.get("wallet");
//		Wallet wallet = Wallet.createFileSystemWallet(walletPath);
//
//		// load a CCP
//		Path networkConfigPath = Paths.get("/home/mikey/DATA/MIKEY/IDEAWorkSpace/ispogsecbob/ispogsecbob-fabric/network/connection/connection-org1.yaml");
//
//		Gateway.Builder builder = Gateway.createBuilder();
//		builder.identity(wallet, "admin").networkConfig(networkConfigPath).discovery(true);
//
//		// create a gateway connection
//		try (Gateway gateway = builder.connect()) {
//
//			// get the network and contract
//			Network network = gateway.getNetwork("mychannel");
//			Contract contract = network.getContract("proof_chaincode");
//
//			byte[] result;
//
//			contract.submitTransaction("deposit", "2020-03-04","filepath","12345678qwertyuiop","owner","overdue");
//
//			result = contract.evaluateTransaction("verify", "123456789qwertyuiop");
//			System.out.println(new String(result));
//
//		}
//	}

	public static void main(String[] args) throws Exception {

		// Load an existing wallet holding identities used to access the network.
		Path walletDirectory = Paths.get("wallet");
		Wallet wallet = null;
		try {
			wallet = Wallet.createFileSystemWallet(walletDirectory);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Path to a common connection profile describing the network.
		Path networkConfigFile = Paths.get("/home/mikey/DATA/MIKEY/IDEAWorkSpace/ispogsecbob/ispogsecbob-fabric/network/connection/connection-org1.yaml");

		// Configure the gateway connection used to access the network.
		Gateway.Builder builder = Gateway.createBuilder()
				.identity(wallet, "admin")
				.networkConfig(networkConfigFile);

		// Create a gateway connection
		try (Gateway gateway = builder.connect()) {

			// Obtain a smart contract deployed on the network.
			Network network = gateway.getNetwork("mychannel");
//			Contract contract = network.getContract("mycc");
			Contract contract = network.getContract("proof_chaincode");

			// Submit transactions that store state to the ledger.
//			byte[] createCarResult = contract.submitTransaction("query", "a");
//			contract.submitTransaction("invoke", "a","b","10");
//			byte[] createCarResult = contract.submitTransaction("query", "a");
			byte[] createCarResult = contract.submitTransaction("verify", "hashcode");
//			byte[] createCarResult = contract.submitTransaction("deposit","2020-08-04","filepath","123456","owner","overdue");
			System.out.println(new String(createCarResult, StandardCharsets.UTF_8));

			// Evaluate transactions that query state from the ledger.
//			byte[] queryAllCarsResult = contract.evaluateTransaction("queryAllCars");
//			System.out.println(new String(queryAllCarsResult, StandardCharsets.UTF_8));

		} catch (ContractException | TimeoutException e) {
			e.printStackTrace();
		}
	}

}
