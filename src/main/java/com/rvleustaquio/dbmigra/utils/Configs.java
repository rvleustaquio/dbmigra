package com.rvleustaquio.dbmigra.utils;

import java.io.*;
import java.util.Properties;

public class Configs {
    Properties prop = new Properties();
    OutputStream output;

    public Configs() {
        File file = new File("config.properties");

        if (file.exists()) {
            try (InputStream input = new FileInputStream(file)) {
                prop.load(input);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            try (OutputStream output = new FileOutputStream("config.properties")) {
                prop.setProperty("ini.prefixo", "betha");
                prop.setProperty("ini.filtro", "audit");
                prop.setProperty("ini.regs-pacote", "5000");
                prop.setProperty("ini.direcao", "1");

                prop.setProperty("sqlsrv.host", "127.0.0.1\\SQLEXPRESS");
                prop.setProperty("sqlsrv.usuario", "sa");
                prop.setProperty("sqlsrv.senha", "b5th4@custom");
                prop.setProperty("sqlsrv.db", "");

                prop.setProperty("syb9.host", "127.0.0.1:6001");
                prop.setProperty("syb9.usuario", "tecbth_rvleustaquio");
                prop.setProperty("syb9.senha", "");
                prop.setProperty("syb9.servico", "pmriofloresrj_trib_serv");
                prop.setProperty("syb9.schema", "bethadba");

                prop.store(output, null);
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }

    public String getPropValues(String field) {
        return prop.getProperty(field);
    }

    public void open() {
        try {
            output = new FileOutputStream("config.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void setPropValues(String field, String value) {
        prop.setProperty(field, value);
    }

    public void save() {
        try {
            prop.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
