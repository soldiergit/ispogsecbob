/*
SPDX-License-Identifier: Apache-2.0
*/

import java.nio.file.Path;
import java.nio.file.Paths;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;

public class ClientApp {

	static {
		System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
	}

	public static void main(String[] args) throws Exception {
		// Load a file system based wallet for managing identities.
		Path walletPath = Paths.get("wallet");
		Wallet wallet = Wallet.createFileSystemWallet(walletPath);

		// load a CCP
		Path networkConfigPath = Paths.get("/home/mikey/DATA/MIKEY/IDEAWorkSpace/ispogsecbob/ispogsecbob-fabric/network/connection/connection-org1.yaml");

		Gateway.Builder builder = Gateway.createBuilder();
		builder.identity(wallet, "user1").networkConfig(networkConfigPath).discovery(true);

		// create a gateway connection
		try (Gateway gateway = builder.connect()) {

			// get the network and contract
			Network network = gateway.getNetwork("mychannel");
			Contract contract = network.getContract("proof_chaincode");

			byte[] result;

//			contract.submitTransaction("deposit", "2020-03-04","filepath","12345678qwertyuiop","owner","overdue");

			result = contract.evaluateTransaction("verify", "123456789qwertyuiop");
			System.out.println(new String(result));

		}
	}

}
