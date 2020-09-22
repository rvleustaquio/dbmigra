package com.rvleustaquio.dbmigra.enums;

public enum FontDad {
	SQLServer(1), SyBase9(2);

	private final int valor;

	FontDad(int vlrOpc) {
		valor = vlrOpc;
	}

	public int getValor() {
		return valor;
	}
}