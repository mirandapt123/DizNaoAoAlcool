package com.example.diznaoaoalcool;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {
  private static final int DATABASE_VERSION = 1;
  private static final String DATABASE_NAME = "diznaoaoalcool";
  private Context context;

  // colocar nome da tabela e das colunas. Exemplo:
  private static final String TABLE_PESSOA = "PESSOA";
  private static final String TABLE_TESTE_ALCOOLEMIA = "TESTE_ALCOOLEMIA";
  private static final String TABLE_BEBIDA_TA = "BEBIDA_TA";
  private static final String TABLE_BEBIDA = "BEBIDA";
  private static final String TABLE_COPO = "COPO";

  // Colocar o código para criar as tabelas. Exemplo:
  private static final String TABLE_CREATE_PESSOA = "CREATE TABLE PESSOA (\n"
      + "    P_ID           INTEGER      PRIMARY KEY AUTOINCREMENT\n" + "                                NOT NULL,\n"
      + "    P_NOME         VARCHAR (30) NOT NULL,\n" + "    P_IDADE        INTEGER      NOT NULL,\n"
      + "    P_PESO         INTEGER       NOT NULL,\n" + "    P_SEXO         INTEGER      NOT NULL,\n"
      + "    P_ANOCARTA     VARCHAR (30) NOT NULL,\n" + "    P_PROFISSIONAL BOOLEAN      NOT NULL,\n"
      + "    P_ACTIVO       BOOLEAN      NOT NULL\n" + ");\n";
  private static final String TABLE_CREATE_TESTE_ALCOOLEMIA = "CREATE TABLE TESTE_ALCOOLEMIA (\n"
      + "    TA_ID        INTEGER  PRIMARY KEY AUTOINCREMENT\n" + "                          NOT NULL,\n"
      + "    P_ID         INTEGER  NOT NULL\n" + "                          REFERENCES PESSOA (P_ID),\n"
      + "    TA_DATA      VARCHAR (30) NOT NULL,\n" + "    TA_RESULTADO DOUBLE   NOT NULL,\n"
      + "    TA_COIMAMAX  VARCHAR (40)   NOT NULL,\n" + "    TA_INIB      VARCHAR (40)  NOT NULL,\n"
      + "    TA_PONTOS    VARCHAR (40)  NOT NULL\n" + ");";
  private static final String TABLE_CREATE_BEBIDA_TA = "CREATE TABLE BEBIDA_TA (\n"
      + "BTA_QUANT     INTEGER  NOT NULL,\n" + "BTA_JEJUM     BOOLEAN  NOT NULL,\n"
      + " B_ID  INTEGER REFERENCES BEBIDA (B_ID) \n" + "                  NOT NULL,\n"
      + " C_ID  INTEGER REFERENCES COPO (C_ID) \n" + "                  NOT NULL,\n" + "TA_ID INTEGER NOT NULL\n"
      + "                  REFERENCES TESTE_ALCOOLEMIA (TA_ID) \n" + ");";
  private static final String TABLE_CREATE_BEBIDA = "CREATE TABLE BEBIDA (B_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, B_TIPO VARCHAR (40) NOT NULL, B_GRADUACAO INTEGER NOT NULL);";
  private static final String TABLE_CREATE_COPO = "CREATE TABLE COPO (C_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, C_TIPO VARCHAR (40) NOT NULL, C_VOLUME INTEGER NOT NULL);";

  /* Constructor */
  public DataBase(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    this.context = context;
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_BEBIDA);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_COPO);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_BEBIDA_TA);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_PESSOA);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_TESTE_ALCOOLEMIA);
    db.execSQL(TABLE_CREATE_BEBIDA);
    db.execSQL(TABLE_CREATE_PESSOA);
    db.execSQL(TABLE_CREATE_TESTE_ALCOOLEMIA);
    db.execSQL(TABLE_CREATE_COPO);
    db.execSQL(TABLE_CREATE_BEBIDA_TA);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_BEBIDA);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_COPO);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_BEBIDA_TA);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_PESSOA);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_TESTE_ALCOOLEMIA);
    onCreate(db);
  }

  // verificar a quantidade de perfis
  public int quantidadePerfil() {
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor c = db.rawQuery("SELECT count(*) FROM " + TABLE_PESSOA, null);
    int i = 0;
    if (c.moveToFirst()) {
      do {
        i = c.getInt(0);
      } while (c.moveToNext());
    }
    c.close();
    db.close();

    return i;
  }

  // lista as bebidas genéricas
  public List<Bebida> listaBebidas() {
    List<Bebida> bebidas = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor c = db.rawQuery("SELECT * FROM BEBIDA WHERE B_ID > 0 AND B_ID < 8", null);
    if (c.moveToFirst()) {
      do {
        Bebida bebida = new Bebida(c.getInt(0), c.getString(1), c.getInt(2));
        bebidas.add(bebida);
      } while (c.moveToNext());
    }
    c.close();
    db.close();
    return bebidas;
  }

  // lista as bebidas/copos detalhadamente de um teste
  public List<BA_TA> listaBebidaCopo(int ta_id) {
    List<BA_TA> bebicopo = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();

    Cursor c = db.rawQuery("SELECT B.B_ID, B.B_TIPO, B.B_GRADUACAO, C.C_ID, C.C_TIPO, C.C_VOLUME FROM " + TABLE_BEBIDA
        + " B LEFT JOIN " + TABLE_BEBIDA_TA + " TA ON B.B_ID = TA.B_ID" + " LEFT JOIN " + TABLE_COPO
        + " C ON C.C_ID = TA.C_ID WHERE TA.TA_ID = " + ta_id + " ORDER BY B.B_GRADUACAO DESC", null);
    if (c.moveToFirst()) {
      do {
        Log.i("Teste duplicado", "B_ID: " + c.getInt(0) + " C_ID: " + c.getInt(3));
        BA_TA bc = new BA_TA(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3), c.getString(4), c.getInt(5));
        bebicopo.add(bc);
      } while (c.moveToNext());
    }
    c.close();
    db.close();
    return bebicopo;
  }

  // obtem todos os testes consoante a ordem
  public List<TA> obtemTA(int idp, String order) {
    List<TA> ta = new ArrayList<>();
    int contador = 0;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor c = db.rawQuery("SELECT * FROM " + TABLE_TESTE_ALCOOLEMIA + " WHERE P_ID = " + idp + " Order by " + order,
        null);
    if (c.moveToFirst()) {
      do {
        TA taobj = new TA(c.getInt(0), c.getInt(1), c.getString(2), c.getDouble(3), c.getString(4), c.getString(5),
            c.getString(6));
        ta.add(taobj);
        contador++;
      } while (c.moveToNext());
    }
    c.close();
    db.close();
    if (contador == 0)
      return null;
    else
      return ta;
  }

  // lista os copos genéricos
  public List<Copo> listaCopos() {
    List<Copo> copos = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor c = db.rawQuery("SELECT * FROM COPO WHERE C_ID > 0 AND C_ID < 8", null);
    if (c.moveToFirst()) {
      do {
        Copo copo = new Copo(c.getInt(0), c.getString(1), c.getInt(2));
        copos.add(copo);
      } while (c.moveToNext());
    }
    c.close();
    db.close();
    return copos;
  }

  // insere uma bebida genérica
  public boolean insereBebidaGenerica(Bebida b) {
    SQLiteDatabase db;

    db = this.getWritableDatabase();
    ContentValues values = new ContentValues();

    long result = -1;

    values.put("B_TIPO", b.getTipo());
    values.put("B_GRADUACAO", b.getGraduacao());
    result = db.insert(TABLE_BEBIDA, null, values);

    db.close();

    if (result == -1) {
      return false;
    } else {
      return true;
    }
  }

  // insere um copo genérico
  public boolean insereCopoGenerico(Copo c) {
    SQLiteDatabase db;

    db = this.getWritableDatabase();
    ContentValues values = new ContentValues();

    long result = -1;

    values.put("C_TIPO", c.getTipo());
    values.put("C_VOLUME", c.getVolume());
    result = db.insert(TABLE_COPO, null, values);

    db.close();

    if (result == -1) {
      return false;
    } else {
      return true;
    }
  }

  // insere um teste
  public int adicionaTA(int id, String data, double taxa, String coima, String pontos, String inib) {
    SQLiteDatabase db;

    db = this.getWritableDatabase();
    ContentValues values = new ContentValues();

    long result = -1;

    values.put("P_ID", id);
    values.put("TA_DATA", data);
    values.put("TA_RESULTADO", taxa);
    values.put("TA_COIMAMAX", coima);
    values.put("TA_INIB", inib);
    values.put("TA_PONTOS", pontos);
    result = db.insert(TABLE_TESTE_ALCOOLEMIA, null, values);

    db.close();

    if (result == -1) {
      return -1;
    } else {
      return obtemLastIDTA();
    }
  }

  // faz update à quantidade de um teste
  public boolean update(int idTA, int quantidade) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL("UPDATE " + TABLE_BEBIDA_TA + " SET BTA_QUANT = BTA_QUANT + " + quantidade + " WHERE TA_ID = " + idTA);
    db.close();
    return false;
  }

  // verifica se já existe uma bebida assim no teste
  private boolean verificatipos(int idBebida, int idCopo, int idTA, int quantidade) {
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor c = db.rawQuery("SELECT count(*) FROM " + TABLE_BEBIDA_TA + " WHERE TA_ID =" + idTA + " AND B_ID = "
        + idBebida + " AND C_ID = " + idCopo, null);
    if (c.moveToFirst()) {
      if (c.getInt(0) == 0) {
        return true;
      } else {
        return update(idTA, quantidade);
      }
    }
    while (c.moveToNext())
      ;
    c.close();
    db.close();
    return false;
  }

  // adiciona a bebida de um teste
  public int adicionaTABC(int idTA, int idBebida, int jejum, int idCopo, int quant) {
    if (verificatipos(idBebida, idCopo, idTA, quant)) {
      SQLiteDatabase db;

      db = this.getWritableDatabase();
      ContentValues values = new ContentValues();

      long result = -1;

      values.put("B_ID", idBebida);
      values.put("TA_ID", idTA);
      values.put("BTA_JEJUM", jejum);
      values.put("BTA_QUANT", quant);
      values.put("C_ID", idCopo);
      result = db.insert(TABLE_BEBIDA_TA, null, values);

      db.close();

      if (result == -1) {
        return -1;
      } else {
        return 1;
      }
    } else {
      return 1;
    }
  }

  // obtem o id do ultimo teste
  private int obtemLastIDTA() {
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor c = db.rawQuery("SELECT * FROM " + TABLE_TESTE_ALCOOLEMIA + " Order by TA_ID DESC", null);
    if (c.moveToFirst()) {
      return c.getInt(0);
    }
    while (c.moveToNext())
      ;

    c.close();
    db.close();
    return -1;
  }

  // obtem a quantidade de um teste com aquela bebida e copo
  public int obtemQuantidade(int ta_id, int b_id, int c_id) {
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor c = db.rawQuery("SELECT BTA_QUANT FROM " + TABLE_BEBIDA_TA + " WHERE TA_ID = " + ta_id + " AND B_ID = "
        + b_id + " AND C_ID = " + c_id, null);
    if (c.moveToFirst()) {
      return c.getInt(0);
    }
    while (c.moveToNext())
      ;

    c.close();
    db.close();
    return 1;
  }

  // adiciona um copo
  public int adicionaCopoCalc(String tipo, int volume) {
    SQLiteDatabase db;

    db = this.getWritableDatabase();
    ContentValues values = new ContentValues();

    long result = -1;

    values.put("C_TIPO", tipo);
    values.put("C_VOLUME", volume);
    result = db.insert(TABLE_COPO, null, values);

    db.close();

    if (result == -1) {
      return -1;
    } else {
      return obtemLastIDCopo();
    }
  }

  // obtem o ultimo id do copo adicionado
  private int obtemLastIDCopo() {
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor c = db.rawQuery("SELECT * FROM " + TABLE_COPO + " Order by C_ID DESC", null);
    if (c.moveToFirst()) {
      return c.getInt(0);
    }
    while (c.moveToNext())
      ;

    c.close();
    db.close();
    return -1;
  }

  // adiciona uma bebida
  public int adicionaBebidaCalc(String tipo, int graduacao) {
    SQLiteDatabase db;

    db = this.getWritableDatabase();
    ContentValues values = new ContentValues();

    long result = -1;

    values.put("B_TIPO", tipo);
    values.put("B_GRADUACAO", graduacao);
    result = db.insert(TABLE_BEBIDA, null, values);

    db.close();

    if (result == -1) {
      return -1;
    } else {
      return obtemLastIDBebida();
    }
  }

  // obtem o id da ultima bebida adicionada
  private int obtemLastIDBebida() {
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor c = db.rawQuery("SELECT * FROM " + TABLE_BEBIDA + " Order by B_ID DESC", null);
    if (c.moveToFirst()) {
      return c.getInt(0);
    }
    while (c.moveToNext())
      ;

    c.close();
    db.close();
    return -1;
  }

  // insere um pessoa
  public boolean inserirPessoa(Pessoa p) {
    SQLiteDatabase db;

    db = this.getWritableDatabase();
    ContentValues values = new ContentValues();

    long result = -1;

    values.put("P_NOME", p.getNome());
    values.put("P_IDADE", p.getIdade());
    values.put("P_PESO", p.getPeso());
    values.put("P_SEXO", p.getSexo());
    values.put("P_ANOCARTA", p.getAno_carta());
    values.put("P_PROFISSIONAL", p.isProfissional());
    values.put("P_ACTIVO", p.isActivo());
    result = db.insert(TABLE_PESSOA, null, values);

    db.close();

    if (result == -1) {
      return false;
    } else {
      return true;
    }
  }

  // altera dados de uma pessoa
  public boolean alterarPessoa(Pessoa p){
    ContentValues values = new ContentValues();
    SQLiteDatabase db = this.getWritableDatabase();

    long result = -1;
    values.put("P_NOME", p.getNome());
    values.put("P_IDADE", p.getIdade());
    values.put("P_PESO", p.getPeso());
    values.put("P_SEXO", p.getSexo());
    values.put("P_ANOCARTA", p.getAno_carta());
    values.put("P_PROFISSIONAL", p.isProfissional());
    values.put("P_ACTIVO", p.isActivo());
    result = db.update(TABLE_PESSOA, values, "P_ID=" + p.getId(), null);

    db.close();

    if(result == -1){
      return false;
    }else {
      return true;
    }
  }

  public boolean verificaNomePessoa(String nome){
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor c = db.rawQuery("SELECT P_NOME FROM " + TABLE_PESSOA + " WHERE P_NOME = '" + nome + "'", null);
    String resultado = null;
    if (c.moveToFirst()) {
      resultado = c.getString(0);
    }
    while (c.moveToNext());

    c.close();
    db.close();

    if(resultado == null){
      return true;
    }else{
      return false;
    }
  }

  // verifica uma bebida existe
  public boolean verificaBebida() {
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor c = db.rawQuery("SELECT count(*) FROM " + TABLE_BEBIDA, null);
    if (c.moveToFirst()) {

      if (c.getInt(0) == 0) {
        return true;
      } else {
        return false;
      }

    }
    while (c.moveToNext())
      ;

    c.close();
    db.close();
    return false;
  }

  // verifica se um copo existe
  public boolean verificaCopo() {
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor c = db.rawQuery("SELECT count(*) FROM " + TABLE_COPO, null);
    if (c.moveToFirst()) {

      if (c.getInt(0) == 0) {
        return true;
      } else {
        return false;
      }

    }
    while (c.moveToNext())
      ;

    c.close();
    db.close();
    return false;
  }

  // lista um perfil activo
  public Pessoa listaPerfilActivo() {
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor c = db.rawQuery("SELECT * FROM " + TABLE_PESSOA + " WHERE P_ACTIVO = 1", null);
    if (c.moveToFirst()) {
      boolean profissional;

      if (c.getInt(6) == 0) {
        profissional = false;
      } else {
        profissional = true;
      }

      return new Pessoa(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getString(5),
          profissional, true);
    }
    while (c.moveToNext())
      ;

    c.close();
    db.close();
    return null;
  }

  // altera o perfil de utilizador ativo
  public boolean alteraPerfilActivo(Pessoa pessoa) {
    int id = listaPerfilActivo().getId();
    ativaDesativaPerfil(id, 0);
    ativaDesativaPerfil(pessoa.getId(), 1);
    return true;
  }

  // ativa ou desativa o perfil de um utilizador
  public boolean ativaDesativaPerfil(int id, int activo){
    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL("UPDATE " + TABLE_PESSOA + " SET P_ACTIVO = " + activo + " WHERE P_ID = " + id);
    db.close();
    return true;
  }

  // lista os perfis
  public List<Pessoa> listaPerfis() {
    List<Pessoa> pessoas = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor c = db.rawQuery("SELECT * FROM Pessoa", null);
    if (c.moveToFirst()) {
      do {
        boolean profissional;
        boolean activo;

        if (c.getInt(6) == 0) {
          profissional = false;
        } else {
          profissional = true;
        }

        if (c.getInt(7) == 0) {
          activo = false;
        } else {
          activo = true;
        }

        Pessoa pessoa = new Pessoa(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getString(5),
            profissional, activo);
        pessoas.add(pessoa);
      } while (c.moveToNext());
    }
    c.close();
    db.close();
    return pessoas;
  }

  // obtem o id de uma bebida com estes parametros
  public int obtemIDBebida(String tipo, int graduacao) {
    SQLiteDatabase db = this.getReadableDatabase();

    Cursor c = db.rawQuery(
        "SELECT B_ID FROM " + TABLE_BEBIDA + " WHERE B_TIPO = '" + tipo + "' AND B_GRADUACAO = " + graduacao, null);
    if (c.moveToFirst()) {
      return c.getInt(0);
    }
    while (c.moveToNext())
      ;

    c.close();
    db.close();
    return -1;
  }

  // obtem o id de um copo com estes parametros
  public int obtemIDCopo(String tipo, int volume) {
    SQLiteDatabase db = this.getReadableDatabase();

    Cursor c = db.rawQuery("SELECT C_ID FROM " + TABLE_COPO + " WHERE C_TIPO = '" + tipo + "' AND C_VOLUME = " + volume,
        null);
    if (c.moveToFirst()) {
      return c.getInt(0);
    }
    while (c.moveToNext())
      ;

    c.close();
    db.close();
    return -1;
  }

  // apaga o histórico
  public void deleteHistory() {
    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL("DELETE FROM " + TABLE_BEBIDA_TA + ";");
    db.execSQL("DELETE FROM " + TABLE_TESTE_ALCOOLEMIA + ";");
    db.close();
  }

  // apaga um teste
  public void delete1History(int ta_id) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL("DELETE FROM " + TABLE_BEBIDA_TA + " WHERE TA_ID = " + ta_id + ";");
    db.execSQL("DELETE FROM " + TABLE_TESTE_ALCOOLEMIA + " WHERE TA_ID = " + ta_id + ";");
    db.close();
  }

}