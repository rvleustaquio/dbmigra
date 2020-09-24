package com.rvleustaquio.dbmigra.model;

import com.rvleustaquio.dbmigra.enums.FontDad;

import java.sql.ResultSet;
import java.util.ArrayList;

public class Tabela {
    FontDad fontDad;
    String schema;
    String nome;
    String prefix;
    ArrayList<Coluna> colunas = new ArrayList<>();

    public Tabela(FontDad fontDad, String schema, String nome, String prefix) {
        this.fontDad = fontDad;
        this.schema = schema;
        this.nome = nome;
        this.prefix = prefix;
    }

    public FontDad getFontDad() {
        return fontDad;
    }

    public String getSchema() {
        return this.schema;
    }
    
    public String getNome() {
        return this.nome;
    }
    
    @Override
    public String toString() {
        return this.schema + "." + this.nome;
    }

    public StringBuilder toSQLDrop() {
        StringBuilder sb = new StringBuilder();

        switch (getFontDad()) {
            case SQLServer:
                sb.append("IF EXISTS (SELECT 1 FROM sysobjects WHERE name = '").append(this.prefix).append(this.schema).append("_").append(this.nome).append("' AND type = 'U') THEN EXECUTE('DROP TABLE ").append(this.prefix).append(this.schema).append("_").append(this.nome).append("'); END IF;");
                break;
            case SyBase9:
                sb.append("DROP TABLE IF EXISTS ").append(this.prefix).append("_").append(this.nome);
                break;
        }

        return sb;
    }
    
    public StringBuilder toSQLCreate() {
        StringBuilder sb = new StringBuilder();
        int colLen = colunas.size();

        switch (getFontDad()) {
            case SQLServer:
                sb.append("CREATE TABLE ").append(this.prefix).append(this.schema).append("_").append(this.nome).append(" (");
                break;
            case SyBase9:
                sb.append("CREATE TABLE ").append(this.prefix).append("_").append(this.nome).append(" (");
                break;
        }

        for(int i = 0; i < colLen; i++) {
            sb.append(colunas.get(i).toSQLCreate());
            if (i != colLen - 1) sb.append(",");
        }
        sb.append(");");
        
        return sb;
    }
    
    public StringBuilder toSQLInsert(ResultSet rs) {
        StringBuilder sb = new StringBuilder();
        int colLen = colunas.size();

        switch (getFontDad()) {
            case SQLServer:
                sb.append("INSERT INTO ").append(this.prefix).append(this.schema).append("_").append(this.nome).append(" (");
                break;
            case SyBase9:
                sb.append("INSERT INTO ").append(this.prefix).append("_").append(this.nome).append(" (");
                break;
        }

        for(int i = 0; i < colLen; i++) {
            sb.append(colunas.get(i).getNome());
            if (i != colLen - 1) sb.append(",");
        }
        sb.append(") VALUES (");
        for(int i = 0; i < colLen; i++) {
            sb.append(colunas.get(i).toSQLInsert(rs));
            if (i != colLen - 1) sb.append(",");
        }
        sb.append(");");
        
        return sb;
    }
    
    public void adicCol(Coluna coluna) {
        colunas.add(coluna);
    }
}