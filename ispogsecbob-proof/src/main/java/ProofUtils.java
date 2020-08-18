import org.hyperledger.fabric.gateway.*;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeoutException;

/**
 * 凭证保存/验证工具
 */
public class ProofUtils {

    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
    }

    private static Logger logger = LoggerFactory.getLogger(ProofUtils.class);
    /**
     * 通道名称
     */
    public static final String CHAIN_NODE_NAME = "mychannel";
    /**
     * 链码名称
     */
    public static final String CONTRACT_NAME = "mycc";
    /**
     * 验证凭证
     */
    public String verifyProof(String hashCode) throws Exception {

        String res = null;

        // Load an existing wallet holding identities used to access the network.
        Path walletDirectory = Paths.get("wallet");

        logger.info("加载钱包");

        Wallet wallet = Wallet.createFileSystemWallet(walletDirectory);

        Path networkConfigFile = Paths.get("/home/mikey/DATA/MIKEY/IDEAWorkSpace/ispogsecbob/ispogsecbob-fabric/network/connection/connection-org1.yaml");

        Gateway.Builder builder = Gateway.createBuilder().identity(wallet, "user1").networkConfig(networkConfigFile);

        try (Gateway gateway = builder.connect()) {

            Network network = gateway.getNetwork(CHAIN_NODE_NAME);

            Contract contract = network.getContract(CONTRACT_NAME);

            byte[] createCarResult = contract.submitTransaction("verify", hashCode);

            res = new String(createCarResult, StandardCharsets.UTF_8);

            logger.info(res);

        } catch (ContractException | TimeoutException e) {
            e.printStackTrace();
        }

        System.out.println(res);

        return res;
    }

        /**
         * 保存凭证
         */
        public String saveProof(String time,String filePath,String hashCode,String owner) throws Exception{

            String res = null;

            logger.info("加载钱包");
            // Load an existing wallet holding identities used to access the network.
            Path walletDirectory = Paths.get("wallet");

            Wallet wallet = Wallet.createFileSystemWallet(walletDirectory);

            Path networkConfigFile = Paths.get("/home/mikey/DATA/MIKEY/IDEAWorkSpace/ispogsecbob/ispogsecbob-fabric/network/connection/connection-org1.yaml");

            Gateway.Builder builder = Gateway.createBuilder().identity(wallet, "user1").networkConfig(networkConfigFile);

            try (Gateway gateway = builder.connect()) {

                Network network = gateway.getNetwork(CHAIN_NODE_NAME);

                Contract contract = network.getContract(CONTRACT_NAME);

                byte[] createCarResult = contract.submitTransaction("deposit", time,filePath,hashCode,owner);

                res = new String(createCarResult, StandardCharsets.UTF_8);

                logger.info(res);

            } catch (ContractException | TimeoutException e) {
                e.printStackTrace();
            }

            System.out.println(res);

            return res;
        }

    /**
     * 注册用户
     */
    public void registerUser() throws Exception{

        // Create a CA client for interacting with the CA.
        Properties props = new Properties();
        props.put("pemFile",
                "/home/mikey/DATA/MIKEY/IDEAWorkSpace/ispogsecbob/ispogsecbob-fabric/network/crypto-config/peerOrganizations/org1.example.com/ca/ca.org1.example.com-cert.pem");
        props.put("allowAllHostNames", "true");
        HFCAClient caClient = HFCAClient.createNewInstance("https://localhost:7054", props);
        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
        caClient.setCryptoSuite(cryptoSuite);

        // Create a wallet for managing identities
        Wallet wallet = Wallet.createFileSystemWallet(Paths.get("wallet"));

        // Check to see if we've already enrolled the user.
        boolean userExists = wallet.exists("user1");
        if (userExists) {
            System.out.println("An identity for the user \"user1\" already exists in the wallet");
            return;
        }

        userExists = wallet.exists("admin");
        if (!userExists) {
            System.out.println("\"admin\" needs to be enrolled and added to the wallet first");
            return;
        }

        Wallet.Identity adminIdentity = wallet.get("admin");
        User admin = new User() {

            @Override
            public String getName() {
                return "admin";
            }

            @Override
            public Set<String> getRoles() {
                return null;
            }

            @Override
            public String getAccount() {
                return null;
            }

            @Override
            public String getAffiliation() {
                return "org1.department1";
            }

            @Override
            public Enrollment getEnrollment() {
                return new Enrollment() {

                    @Override
                    public PrivateKey getKey() {
                        return adminIdentity.getPrivateKey();
                    }

                    @Override
                    public String getCert() {
                        return adminIdentity.getCertificate();
                    }
                };
            }

            @Override
            public String getMspId() {
                return "Org1MSP";
            }

        };

        // Register the user, enroll the user, and import the new identity into the wallet.
        RegistrationRequest registrationRequest = new RegistrationRequest("user1");
        registrationRequest.setAffiliation("org1.department1");
        registrationRequest.setEnrollmentID("user1");
        String enrollmentSecret = caClient.register(registrationRequest, admin);
        Enrollment enrollment = caClient.enroll("user1", enrollmentSecret);
        Wallet.Identity user = Wallet.Identity.createIdentity("Org1MSP", enrollment.getCert(), enrollment.getKey());
        wallet.put("user1", user);
        System.out.println("Successfully enrolled user \"user1\" and imported it into the wallet");

    }

    /**
     * 注册管理员
     * @throws Exception
     */
    public void enroolAdmin() throws Exception {
        // Create a CA client for interacting with the CA.
        Properties props = new Properties();
        props.put("pemFile", "/home/mikey/DATA/MIKEY/IDEAWorkSpace/ispogsecbob/ispogsecbob-fabric/network/crypto-config/peerOrganizations/org1.example.com/ca/ca.org1.example.com-cert.pem");
        props.put("allowAllHostNames", "true");
        HFCAClient caClient = HFCAClient.createNewInstance("https://localhost:7054", props);
        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
        caClient.setCryptoSuite(cryptoSuite);

        // Create a wallet for managing identities
        Wallet wallet = Wallet.createFileSystemWallet(Paths.get("wallet"));

        // Check to see if we've already enrolled the admin user.
        boolean adminExists = wallet.exists("admin");
        if (adminExists) {
            System.out.println("An identity for the admin user \"admin\" already exists in the wallet");
            return;
        }

        // Enroll the admin user, and import the new identity into the wallet.
        final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
        enrollmentRequestTLS.addHost("localhost");
        enrollmentRequestTLS.setProfile("tls");
        Enrollment enrollment = caClient.enroll("admin", "adminpw", enrollmentRequestTLS);
        Wallet.Identity user = Wallet.Identity.createIdentity("Org1MSP", enrollment.getCert(), enrollment.getKey());
        wallet.put("admin", user);
        System.out.println("Successfully enrolled user \"admin\" and imported it into the wallet");
    }

    public static void main(String[] args) throws Exception{
        ProofUtils proofUtils = new ProofUtils();
//        proofUtils.enroolAdmin();
//        proofUtils.registerUser();
        proofUtils.verifyProof("hashcode");
        proofUtils.verifyProof("123456");
    }
}
