/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.frazao.neuralnetwork.utils;

import br.com.frazo.neuralnets.utils.NNFeeder;
import br.com.frazao.neuralnetwork.CobrancaPOJO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Ygor
 */
public class CobrancaFeeder implements NNFeeder{
    
    public static final String QUERY_COBRANCA_PAGANTES = "SELECT \n" +
"	ecc.ecco_dtenvioconta AS \"DATA ENVIO PARA COBRANCA\",\n" +
"	ecc.imov_id AS \"IMOVEL\",\n" +
"	CASE cli.clie_iccpfcnpjvalidado \n" +
"		WHEN 1 THEN 0.8 \n" +
"		ELSE 0.2\n" +
"	END AS \"DOC VALIDADO\",\n" +
"	imo.iper_id/12.0 AS \"PERFIL\",\n" +
"	imo.imov_idcategoriaprincipal/6.0 AS \"CATEGORIA PRINCIPAL\",\n" +
"	ftb.ftab_id/12.0 AS \"TIPO DO ABASTECIMENTO\",\n" +
"	imo.loca_id/1100.0 AS \"ID LOCALIDADE\",\n" +
"	imo.last_id/12.0 AS \"ID SIT. AGUA\",\n" +
"	imo.lest_id/12.0 AS \"ID SIT. ESG\",\n" +
"	CASE\n" +
"		WHEN NOT hid.hidr_nnhidrometro IS NULL THEN 0.8\n" +
"		ELSE 0.2 \n" +
"	END AS \"HIDROMETRO\",\n" +
"	COUNT(ecc.ecco_id) AS \"CONTAS ENVIADAS\",\n" +
"	COUNT(ecp.eccp_ictipopagamento) AS \"CONTAS PAGAS\",\n" +
"	COUNT(ecp.eccp_ictipopagamento)::FLOAT/COUNT(ecc.ecco_id)::FLOAT AS \"PERCENTUAL PAGO\"\n" +
"FROM \n" +
"	cobranca.empresa_cobranca_conta ecc \n" +
"	LEFT JOIN cobranca.empr_cobr_conta_pagto ecp ON ecc.ecco_id = ecp.ecco_id\n" +
"	LEFT JOIN cadastro.imovel imo ON imo.imov_id = ecc.imov_id\n" +
"	LEFT JOIN cadastro.cliente_imovel cim ON cim.imov_id = imo.imov_id AND cim.clim_dtrelacaofim IS NULL AND cim.clim_icnomeconta = 1\n" +
"	LEFT JOIN cadastro.cliente cli ON cli.clie_id = cim.clie_id\n" +
"	LEFT JOIN cadastro.fonte_abastecimento ftb ON ftb.ftab_id = imo.ftab_id\n" +
"	LEFT JOIN atendimentopublico.ligacao_agua lagu ON lagu.lagu_id = imo.imov_id\n" +
"	LEFT JOIN micromedicao.hidrometro_inst_hist his ON lagu.hidi_id = his.hidi_id AND his.hidi_dtretiradahidrometro IS NULL\n" +
"	LEFT JOIN micromedicao.hidrometro hid ON his.hidr_id = hid.hidr_id\n" +
"WHERE \n" +
"	--ecp.eccp_ampagamento >= 202101 AND ecp.eccp_ampagamento <= 202112 AND --imo.imov_id = 3980669 AND\n" +
"	--((emp.empr_id >= 34 AND emp.empr_id <=42 AND ecp.eccp_dtpagamento > '2019-05-01') OR\n" +
"	--(emp.empr_id = 33 AND ecp.eccp_dtpagamento > '2019-05-09') OR\n" +
"	--(emp.empr_id >= 29 AND emp.empr_id <= 32 AND ecp.eccp_dtpagamento > '2019-04-22'))\n" +
"	ecc.ecco_dtenvioconta >= '2019-01-01'\n" +
"	AND NOT ecc.cnta_id IS NULL\n" +
"GROUP BY 1,2,3,4,5,6,7,8,9,10 HAVING COUNT(ecp.eccp_ictipopagamento)::FLOAT/COUNT(ecc.ecco_id)> 0\n" +
"ORDER BY RANDOM() \n" +
"LIMIT 10000";
    
//    public static final String QUERY_COBRANCA_PAGANTES_NAO_PAGANTES = "	(SELECT \n" +
//"		ecc.ecco_dtenvioconta AS \"DATA ENVIO PARA COBRANCA\",\n" +
//"		ecc.imov_id AS \"IMOVEL\",\n" +
//"		CASE cli.clie_iccpfcnpjvalidado \n" +
//"			WHEN 1 THEN 0.8 \n" +
//"			ELSE 0.2\n" +
//"		END AS \"DOC VALIDADO\",\n" +
//"		imo.iper_id/12.0 AS \"PERFIL\",\n" +
//"		imo.imov_idcategoriaprincipal/6.0 AS \"CATEGORIA PRINCIPAL\",\n" +
//"		ftb.ftab_id/12.0 AS \"TIPO DO ABASTECIMENTO\",\n" +
//"		imo.loca_id/1100.0 AS \"ID LOCALIDADE\",\n" +
//"		imo.last_id/12.0 AS \"ID SIT. AGUA\",\n" +
//"		imo.lest_id/12.0 AS \"ID SIT. ESG\",\n" +
//"		CASE\n" +
//"			WHEN NOT hid.hidr_nnhidrometro IS NULL THEN 0.8\n" +
//"			ELSE 0.2 \n" +
//"		END AS \"HIDROMETRO\",\n" +
//"		COUNT(ecc.ecco_id) AS \"CONTAS ENVIADAS\",\n" +
//"		COUNT(ecp.eccp_ictipopagamento) AS \"CONTAS PAGAS\",\n" +
//"		1::FLOAT AS \"PERCENTUAL PAGO\"\n" +
//"	FROM \n" +
//"		cobranca.empresa_cobranca_conta ecc \n" +
//"		LEFT JOIN cobranca.empr_cobr_conta_pagto ecp ON ecc.ecco_id = ecp.ecco_id\n" +
//"		LEFT JOIN cadastro.imovel imo ON imo.imov_id = ecc.imov_id\n" +
//"		LEFT JOIN cadastro.cliente_imovel cim ON cim.imov_id = imo.imov_id AND cim.clim_dtrelacaofim IS NULL AND cim.clim_icnomeconta = 1\n" +
//"		LEFT JOIN cadastro.cliente cli ON cli.clie_id = cim.clie_id\n" +
//"		LEFT JOIN cadastro.fonte_abastecimento ftb ON ftb.ftab_id = imo.ftab_id\n" +
//"		LEFT JOIN atendimentopublico.ligacao_agua lagu ON lagu.lagu_id = imo.imov_id\n" +
//"		LEFT JOIN micromedicao.hidrometro_inst_hist his ON lagu.hidi_id = his.hidi_id AND his.hidi_dtretiradahidrometro IS NULL\n" +
//"		LEFT JOIN micromedicao.hidrometro hid ON his.hidr_id = hid.hidr_id\n" +
//"	WHERE \n" +
//"		--ecp.eccp_ampagamento >= 202101 AND ecp.eccp_ampagamento <= 202112 AND --imo.imov_id = 3980669 AND\n" +
//"		--((emp.empr_id >= 34 AND emp.empr_id <=42 AND ecp.eccp_dtpagamento > '2019-05-01') OR\n" +
//"		--(emp.empr_id = 33 AND ecp.eccp_dtpagamento > '2019-05-09') OR\n" +
//"		--(emp.empr_id >= 29 AND emp.empr_id <= 32 AND ecp.eccp_dtpagamento > '2019-04-22'))\n" +
//"		ecc.ecco_dtenvioconta >= '2019-01-01'\n" +
//"		AND NOT ecc.cnta_id IS NULL\n" +
//"	GROUP BY 1,2,3,4,5,6,7,8,9,10 HAVING COUNT(ecp.eccp_ictipopagamento)::FLOAT/COUNT(ecc.ecco_id)> 0\n" +
//"	ORDER BY RANDOM()\n" +
//"	LIMIT 100000)\n" +
//"UNION\n" +
//"	(SELECT \n" +
//"		ecc.ecco_dtenvioconta AS \"DATA ENVIO PARA COBRANCA\",\n" +
//"		ecc.imov_id AS \"IMOVEL\",\n" +
//"		CASE cli.clie_iccpfcnpjvalidado \n" +
//"			WHEN 1 THEN 0.8 \n" +
//"			ELSE 0.2\n" +
//"		END AS \"DOC VALIDADO\",\n" +
//"		imo.iper_id/12.0 AS \"PERFIL\",\n" +
//"		imo.imov_idcategoriaprincipal/6.0 AS \"CATEGORIA PRINCIPAL\",\n" +
//"		ftb.ftab_id/12.0 AS \"TIPO DO ABASTECIMENTO\",\n" +
//"		imo.loca_id/1100.0 AS \"ID LOCALIDADE\",\n" +
//"		imo.last_id/12.0 AS \"ID SIT. AGUA\",\n" +
//"		imo.lest_id/12.0 AS \"ID SIT. ESG\",\n" +
//"		CASE\n" +
//"			WHEN NOT hid.hidr_nnhidrometro IS NULL THEN 0.8\n" +
//"			ELSE 0.2 \n" +
//"		END AS \"HIDROMETRO\",\n" +
//"		COUNT(ecc.ecco_id) AS \"CONTAS ENVIADAS\",\n" +
//"		COUNT(ecp.eccp_ictipopagamento) AS \"CONTAS PAGAS\",\n" +
//"		0::FLOAT AS \"PERCENTUAL PAGO\"\n" +
//"	FROM \n" +
//"		cobranca.empresa_cobranca_conta ecc \n" +
//"		LEFT JOIN cobranca.empr_cobr_conta_pagto ecp ON ecc.ecco_id = ecp.ecco_id\n" +
//"		LEFT JOIN cadastro.imovel imo ON imo.imov_id = ecc.imov_id\n" +
//"		LEFT JOIN cadastro.cliente_imovel cim ON cim.imov_id = imo.imov_id AND cim.clim_dtrelacaofim IS NULL AND cim.clim_icnomeconta = 1\n" +
//"		LEFT JOIN cadastro.cliente cli ON cli.clie_id = cim.clie_id\n" +
//"		LEFT JOIN cadastro.fonte_abastecimento ftb ON ftb.ftab_id = imo.ftab_id\n" +
//"		LEFT JOIN atendimentopublico.ligacao_agua lagu ON lagu.lagu_id = imo.imov_id\n" +
//"		LEFT JOIN micromedicao.hidrometro_inst_hist his ON lagu.hidi_id = his.hidi_id AND his.hidi_dtretiradahidrometro IS NULL\n" +
//"		LEFT JOIN micromedicao.hidrometro hid ON his.hidr_id = hid.hidr_id\n" +
//"	WHERE \n" +
//"		--ecp.eccp_ampagamento >= 202101 AND ecp.eccp_ampagamento <= 202112 AND --imo.imov_id = 3980669 AND\n" +
//"		--((emp.empr_id >= 34 AND emp.empr_id <=42 AND ecp.eccp_dtpagamento > '2019-05-01') OR\n" +
//"		--(emp.empr_id = 33 AND ecp.eccp_dtpagamento > '2019-05-09') OR\n" +
//"		--(emp.empr_id >= 29 AND emp.empr_id <= 32 AND ecp.eccp_dtpagamento > '2019-04-22'))\n" +
//"		ecc.ecco_dtenvioconta >= '2019-01-01'\n" +
//"		AND NOT ecc.cnta_id IS NULL\n" +
//"	GROUP BY 1,2,3,4,5,6,7,8,9,10 HAVING COUNT(ecp.eccp_ictipopagamento)::FLOAT/COUNT(ecc.ecco_id)= 0\n" +
//"	ORDER BY RANDOM() \n" +
//"	LIMIT 100000)";
    
    public static final String QUERY_COBRANCA_PAGANTES_NAO_PAGANTES = "SELECT \n" +
"	ecc.ecco_dtenvioconta AS \"DATA ENVIO PARA COBRANCA\",\n" +
"	ecc.imov_id AS \"IMOVEL\",\n" +
"	CASE cli.clie_iccpfcnpjvalidado \n" +
"		WHEN 1 THEN 0.8 \n" +
"		ELSE 0.2\n" +
"	END AS \"DOC VALIDADO\",\n" +
"	imo.iper_id/12.0 AS \"PERFIL\",\n" +
"	imo.imov_idcategoriaprincipal/6.0 AS \"CATEGORIA PRINCIPAL\",\n" +
"	ftb.ftab_id/12.0 AS \"TIPO DO ABASTECIMENTO\",\n" +
"	imo.loca_id/1100.0 AS \"ID LOCALIDADE\",\n" +
"	imo.last_id/12.0 AS \"ID SIT. AGUA\",\n" +
"	imo.lest_id/12.0 AS \"ID SIT. ESG\",\n" +
"	CASE\n" +
"		WHEN NOT hid.hidr_nnhidrometro IS NULL THEN 0.8\n" +
"		ELSE 0.2 \n" +
"	END AS \"HIDROMETRO\",\n" +
"	COUNT(ecc.ecco_id) AS \"CONTAS ENVIADAS\",\n" +
"	COUNT(ecp.eccp_ictipopagamento) AS \"CONTAS PAGAS\",\n" +
"	COUNT(ecp.eccp_ictipopagamento)::FLOAT/COUNT(ecc.ecco_id)::FLOAT AS \"PERCENTUAL PAGO\"\n" +
"FROM \n" +
"	cobranca.empresa_cobranca_conta ecc \n" +
"	LEFT JOIN cobranca.empr_cobr_conta_pagto ecp ON ecc.ecco_id = ecp.ecco_id\n" +
"	LEFT JOIN cadastro.imovel imo ON imo.imov_id = ecc.imov_id\n" +
"	LEFT JOIN cadastro.cliente_imovel cim ON cim.imov_id = imo.imov_id AND cim.clim_dtrelacaofim IS NULL AND cim.clim_icnomeconta = 1\n" +
"	LEFT JOIN cadastro.cliente cli ON cli.clie_id = cim.clie_id\n" +
"	LEFT JOIN cadastro.fonte_abastecimento ftb ON ftb.ftab_id = imo.ftab_id\n" +
"	LEFT JOIN atendimentopublico.ligacao_agua lagu ON lagu.lagu_id = imo.imov_id\n" +
"	LEFT JOIN micromedicao.hidrometro_inst_hist his ON lagu.hidi_id = his.hidi_id AND his.hidi_dtretiradahidrometro IS NULL\n" +
"	LEFT JOIN micromedicao.hidrometro hid ON his.hidr_id = hid.hidr_id\n" +
"WHERE \n" +
"	--ecp.eccp_ampagamento >= 202101 AND ecp.eccp_ampagamento <= 202112 AND --imo.imov_id = 3980669 AND\n" +
"	--((emp.empr_id >= 34 AND emp.empr_id <=42 AND ecp.eccp_dtpagamento > '2019-05-01') OR\n" +
"	--(emp.empr_id = 33 AND ecp.eccp_dtpagamento > '2019-05-09') OR\n" +
"	--(emp.empr_id >= 29 AND emp.empr_id <= 32 AND ecp.eccp_dtpagamento > '2019-04-22'))\n" +
"	ecc.ecco_dtenvioconta >= '2019-01-01'\n" +
"	AND NOT ecc.cnta_id IS NULL\n" +
"GROUP BY 1,2,3,4,5,6,7,8,9,10\n" +
"ORDER BY RANDOM() \n" +
"LIMIT 700000";

    public static final String DB_DRIVER = "org.postgresql.Driver";
    public static final String DB_URL = "jdbc:postgresql://10.39.192.21:5432/gsan_comercial";
    public static final String DB_USER = "ap_comercial";
    public static final String DB_PASS = "ap#10#c1000";
    
    private final ArrayList<CobrancaPOJO> dataset = new ArrayList<>();
    private int currentPosition = -1;

    public CobrancaFeeder() throws SQLException, ClassNotFoundException {        
        try (Connection con = conectarDB(DB_DRIVER, DB_URL, DB_USER, DB_PASS)) {
            String query=QUERY_COBRANCA_PAGANTES_NAO_PAGANTES;
            ResultSet resultSet = con.createStatement().executeQuery(query);
            while(resultSet.next())
            {
                CobrancaPOJO cobrancaPOJO;
                cobrancaPOJO = new CobrancaPOJO(resultSet.getDate(1), resultSet.getInt(2), resultSet.getDouble(3), resultSet.getDouble(4), resultSet.getDouble(5), resultSet.getDouble(6), resultSet.getDouble(7), resultSet.getDouble(8), resultSet.getDouble(9), resultSet.getDouble(10), resultSet.getDouble(11), resultSet.getInt(12), resultSet.getDouble(13));
                dataset.add(cobrancaPOJO);
            }
            Collections.shuffle(dataset);
        }
    }
    
    @Override
    public double[] getNextInput() {
        currentPosition++;
        if(currentPosition>=dataset.size())
            currentPosition=0;
        CobrancaPOJO cobranca = dataset.get(currentPosition);
        double[] ret = new double[8];
        ret[0] = cobranca.getDocValid();
        ret[1] = cobranca.getPerfil();
        ret[2] = cobranca.getCategoria();
        ret[3] = cobranca.getAbastecimento();
        ret[4] = cobranca.getSitAgua();
        ret[5] = cobranca.getSitEsg();
        ret[6] = cobranca.getHd();
        ret[7] = cobranca.getEnviadas();
        //ret[4] = cobranca.getLocalidade();
        return ret;
    }

    @Override
    public double[] getExpectedResult() {
        double[] ret = new double[1];
        double percentualPago = dataset.get(currentPosition).getPropPag();
//        if(percentualPago > 0){
//            ret[0] = 1;
//            ret[1] = 0;
//        }else{
//            ret[0] = 0;
//            ret[1] = 1;
//        }
        ret[0] = percentualPago;
        return ret;
    }

    @Override
    public int getSize() {
        return dataset.size();
    }

    @Override
    public int getCurrentTupla() {
        return currentPosition;
    }
    
    public final Connection conectarDB(String driver, String url, String user, String pass) throws ClassNotFoundException, SQLException {
        Connection con;
        Class.forName(driver);
        con = DriverManager.getConnection(url, user, pass);
        return con;
    }
    
}
