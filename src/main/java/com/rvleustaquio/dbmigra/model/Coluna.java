package com.rvleustaquio.dbmigra.model;

import com.rvleustaquio.dbmigra.enums.FontDad;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Coluna {

    FontDad fontDad;
    String nome;
    String tipo;
    int tamanho;
    boolean nullable;
    int precisao;

    public Coluna(FontDad fontDad, ResultSet rsCol) {
        try {
            this.fontDad = fontDad;
            this.nome = "[" + rsCol.getString("COLUMN_NAME") + "]";
            this.tipo = rsCol.getString("TYPE_NAME").replace("(", "").replace(")", "").replace("identity", "").trim();
            this.tamanho = rsCol.getInt("COLUMN_SIZE");
            this.precisao = rsCol.getInt("DECIMAL_DIGITS");
            switch (rsCol.getInt("NULLABLE")) {
                case 0:
                    this.nullable = false;
                    break;
                case 1:
                    this.nullable = true;
                    break;
            }
        } catch (SQLException ignored) {
        }
    }

    public FontDad getFontDad() {
        return fontDad;
    }

    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
    }

    public int getTamanho() {
        return tamanho;
    }

    public boolean isNotNullable() {
        return !nullable;
    }

    public int getPrecisao() {
        return precisao;
    }

    public String toSQLCreate() {
        String rs = null;

        switch (this.getFontDad()) {
            case SQLServer:
                switch (this.getTipo()) {
                    case "tinyint":
                    case "smallint":
                    case "int":
                    case "bigint":
                    case "date":
                    case "time":
                    case "bit":
                    case "datetime":
                    case "smalldatetime":
                    case "text":
                    case "float":
                    case "double":
                    case "money":
                    case "image":
                    case "timestamp":
                    case "real":
                        rs = this.getNome() + " " + this.getTipo() + (this.isNotNullable() ? " NOT NULL" : " NULL");
                        break;
                    case "decimal":
                    case "numeric":
                        rs = this.getNome() + " " + this.getTipo() + "(" + this.getTamanho() + "," + this.getPrecisao()
                                + ")" + (this.isNotNullable() ? " NOT NULL" : " NULL");
                        break;
                    case "nchar":
                    case "nvarchar":
                        rs = this.getNome() + " VARCHAR(9999)" + (this.isNotNullable() ? " NOT NULL" : " NULL");
                        break;
                    case "varbinary":
                        rs = this.getNome() + " VARBINARY(9999)" + (this.isNotNullable() ? " NOT NULL" : " NULL");
                        break;
                    case "ntext":
                        rs = this.getNome() + " TEXT" + (this.isNotNullable() ? " NOT NULL" : " NULL");
                        break;
                    case "uniqueidentifier":
                        rs = this.getNome() + " VARCHAR(99)" + (this.isNotNullable() ? " NOT NULL" : " NULL");
                        break;
                    default:
                        rs = this.getNome() + " " + this.getTipo() + "("
                                + (Math.min(this.getTamanho(), 9999)) + ")"
                                + (this.isNotNullable() ? " NOT NULL" : " NULL");
                        break;
                }
                break;
            case SyBase9:
                switch (this.getTipo()) {
                    case "tinyint":
                    case "smallint":
                    case "int":
                    case "bigint":
                    case "date":
                    case "time":
                    case "bit":
                    case "datetime":
                    case "smalldatetime":
                    case "text":
                    case "float":
                    case "double":
                    case "money":
                    case "image":
                    case "real":
                        rs = this.getNome() + " " + this.getTipo() + (this.isNotNullable() ? " NOT NULL" : " NULL");
                        break;
                    case "decimal":
                    case "numeric":
                        rs = this.getNome() + " " + this.getTipo() + "(" + this.getTamanho() + "," + this.getPrecisao()
                                + ")" + (this.isNotNullable() ? " NOT NULL" : " NULL");
                        break;
                    case "nchar":
                    case "long varchar":
                        rs = this.getNome() + " NVARCHAR(max)" + (this.isNotNullable() ? " NOT NULL" : " NULL");
                        break;
                    case "varbinary":
                        rs = this.getNome() + " VARBINARY(max)" + (this.isNotNullable() ? " NOT NULL" : " NULL");
                        break;
                    case "ntext":
                        rs = this.getNome() + " TEXT" + (this.isNotNullable() ? " NOT NULL" : " NULL");
                        break;
                    case "uniqueidentifier":
                        rs = this.getNome() + " VARCHAR(99)" + (this.isNotNullable() ? " NOT NULL" : " NULL");
                        break;
                    case "timestamp":
                        rs = this.getNome() + " DATETIME" + (this.isNotNullable() ? " NOT NULL" : " NULL");
                        break;
                    default:
                        rs = this.getNome() + " " + this.getTipo() + "("
                                + (Math.min(this.getTamanho(), 9999)) + ")"
                                + (this.isNotNullable() ? " NOT NULL" : " NULL");
                        break;
                }
                break;
        }
        return rs;
    }

    public String toSQLInsert(ResultSet rs) {
        String str = null;

        try {
            switch (this.getFontDad()) {
                case SQLServer:
                    str = rs.getString(this.getNome().replace("[", "").replace("]", "")).replace("'", "");
                    switch (this.getTipo()) {
                        case "char":
                        case "varchar":
                        case "nchar":
                        case "nvarchar":
                        case "date":
                        case "time":
                        case "datetime":
                        case "smalldatetime":
                        case "timestamp":
                        case "varbinary":
                        case "text":
                        case "ntext":
                        case "image":
                        case "uniqueidentifier":
                            str = "'" + str + "'";
                            break;
                    }
                    str = str.replace(Character.toString((char) 65533), "");
                    break;
                case SyBase9:
                    str = rs.getString(this.getNome().replace("[", "").replace("]", "")).replace("'", "");
                    switch (this.getTipo()) {
                        case "char":
                        case "varchar":
                        case "long varchar":
                        case "varbinary":
                        case "text":
                        case "image":
                        case "uniqueidentifier":
                        case "date":
                        case "time":
                        case "datetime":
                        case "smalldatetime":
                            str = "'" + str + "'";
                            break;
                        case "timestamp":
                            str = "convert(datetime, '" + str + "')";
                            break;
                    }
                    str = str.replace(Character.toString((char) 65533), "");
                    break;
            }
        } catch (Exception e) {
            str = "null";
        }

        return str;
    }
}