package cl.lay.azurevmoff;

import android.content.Context;

import cl.lay.azurevmoff.repositories.AccountRepository;
import cl.lay.azurevmoff.services.AzureManagementApiService;
import cl.lay.azurevmoff.services.KeyStoreService;

public class ApplicationState {

       // Singleton implementation
    private static ApplicationState instance = null;
    public static ApplicationState getInstance() {
        if (instance == null) {
            instance = new ApplicationState();
        }
        return instance;
    }


    private KeyStoreService keyStoreService = null;
    private AzureManagementApiService azureManagementApiService=null;
    private AccountRepository accountRepository;

    public KeyStoreService getKeyStoreService() {

        if (keyStoreService == null) {
            keyStoreService = new KeyStoreService();
        }
        return keyStoreService;
    }

    public AzureManagementApiService getAzureManagmentApiService(){

        if(azureManagementApiService==null){
            azureManagementApiService = new AzureManagementApiService();
        }
        return azureManagementApiService;
    }

    public AccountRepository getAccountRepository(Context context){
        if(accountRepository==null) {
            accountRepository = new AccountRepository(context.getApplicationContext());
        }
        return accountRepository;
    }


}
