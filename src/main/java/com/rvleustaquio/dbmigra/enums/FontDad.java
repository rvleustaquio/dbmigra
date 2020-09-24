package com.rvleustaquio.dbmigra.enums;

public enum FontDad {
    SQLServer(1), SyBase9(2);

    private final int id;

    FontDad(int vlrOpc) {
        id = vlrOpc;
    }

    public int getId() {
        return id;
    }

    public static FontDad fromId(int id) {
        for (FontDad type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return null;
    }
}