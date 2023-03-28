/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.frazao.neuralnetwork;

import br.com.frazao.neuralnetwork.activationfunctions.SigmoidActivator;
import br.com.frazao.neuralnetwork.errorfunctions.OneHalfMeanSquaredError;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ygorl
 */
public class Controle {
    
    
    public static final String DB_DRIVER = "org.postgresql.Driver";
    public static final String DB_URL = "jdbc:postgresql://10.39.192.21:5432/gsan_comercial";
    public static final String DB_USER = "ap_comercial";
    public static final String DB_PASS = "ap#10#c1000";
    private static final String TRAINING_DATA_QUERY = "SELECT r1.*\n" +
    "  FROM forex_data.history AS r1 JOIN\n" +
    "       (SELECT CEIL(RAND() *\n" +
    "                     (SELECT MAX(id)\n" +
    "                        FROM forex_data.history)+11) AS id)\n" +
    "        AS r2\n" +
    " WHERE r1.id <= r2.id AND r1.id>=r2.id-10\n" +
    " ORDER BY datetime DESC";
    
    public static final String QUERY_SELECT_COBRANCA_SET_1 = "SELECT \n" +
                            "	ecc.ecco_dtenvioconta AS \"DATA ENVIO\",\n" +
                            "	ecc.imov_id AS \"IMOVEL\",\n" +
                            "	cli.clie_iccpfcnpjvalidado/2.0 AS \"DOC VALID\",\n" +
                            "	imo.iper_id/9.0 AS \"PERFIL\",\n" +
                            "	imo.imov_idcategoriaprincipal/4.0 AS \"CATEGORIA PRINCIPAL\",\n" +
                            "	ftb.ftab_id/5.0 AS \"ABASTECIMENTO\",\n" +
                            "	imo.loca_id/927.0 AS \"LOCALIDADE\",\n" +
                            "	imo.last_id/8.0 AS \"ID SIT. AGUA\",\n" +
                            "	imo.lest_id/6.0 AS \"ID SIT. ESG\",\n" +
                            "	CASE\n" +
                            "		WHEN NOT hid.hidr_nnhidrometro IS NULL THEN 1\n" +
                            "		ELSE 0.5\n" +
                            "	END AS \"HD\",\n" +
                            "	COUNT(ecc.ecco_id)/120.0 AS \"ENVIADAS\",\n" +
                            "	COUNT(ecp.eccp_ictipopagamento) AS \"PAGAS\",\n" +
                            "	COUNT(ecp.eccp_ictipopagamento)::FLOAT/COUNT(ecc.ecco_id)::FLOAT AS \"PROP_PAG\"\n" +
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
                            "	ecc.ecco_dtenvioconta IN (VAR_DATE)\n" +
                            "	AND NOT ecc.cnta_id IS NULL\n" +
                            "GROUP BY 1,2,3,4,5,6,7,8,9,10\n" +
                            "ORDER BY RANDOM() DESC\n" +
                            "--LIMIT 10";
    
    public static final String QUERY_SELECT_COBRANCA_SET_2 = "SELECT \n" +
                            "	ecc.ecco_dtenvioconta AS \"DATA ENVIO PARA COBRANCA\",\n" +
                            "	ecc.imov_id AS \"IMOVEL\",\n" +
                            "	cli.clie_iccpfcnpjvalidado AS \"DOC VALIDADO\",\n" +
                            "	imo.iper_id AS \"PERFIL\",\n" +
                            "	imo.imov_idcategoriaprincipal AS \"CATEGORIA PRINCIPAL\",\n" +
                            "	ftb.ftab_id AS \"TIPO DO ABASTECIMENTO\",\n" +
                            "	imo.loca_id AS \"ID LOCALIDADE\",\n" +
                            "	imo.last_id AS \"ID SIT. AGUA\",\n" +
                            "	imo.lest_id AS \"ID SIT. ESG\",\n" +
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
                            "	ecc.ecco_dtenvioconta IN (VAR_DATE)\n" +
                            "	AND NOT ecc.cnta_id IS NULL\n" +
                            "GROUP BY 1,2,3,4,5,6,7,8,9,10 \n" +
                            "ORDER BY RANDOM() DESC \n" +
                            "--LIMIT 10";
    
    public static final String QUERY_SELECT_COBRANCA_SET_3 = "SELECT \n" +
                                                        "	ecc.ecco_dtenvioconta AS \"DATA ENVIO PARA COBRANCA\",\n" +
                                                        "	ecc.imov_id AS \"IMOVEL\",\n" +
                                                        "	cli.clie_iccpfcnpjvalidado AS \"DOC VALIDADO\",\n" +
                                                        "	imo.iper_id AS \"PERFIL\",\n" +
                                                        "	imo.imov_idcategoriaprincipal AS \"CATEGORIA PRINCIPAL\",\n" +
                                                        "	ftb.ftab_id AS \"TIPO DO ABASTECIMENTO\",\n" +
                                                        "	imo.loca_id AS \"ID LOCALIDADE\",\n" +
                                                        "	imo.last_id AS \"ID SIT. AGUA\",\n" +
                                                        "	imo.lest_id AS \"ID SIT. ESG\",\n" +
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
                                                        "GROUP BY 1,2,3,4,5,6,7,8,9,10 HAVING COUNT(ecp.eccp_ictipopagamento)::FLOAT/COUNT(ecc.ecco_id)> 0 AND COUNT(ecp.eccp_ictipopagamento)::FLOAT/COUNT(ecc.ecco_id) < 1\n" +
                                                        "ORDER BY RANDOM() \n" +
                                                        "--LIMIT 10";
    
    public static final String QUERY_SELECT_COBRANCA_SET_4 = "SELECT \n" +
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
                        "GROUP BY 1,2,3,4,5,6,7,8,9,10 HAVING COUNT(ecp.eccp_ictipopagamento)::FLOAT/COUNT(ecc.ecco_id)> 0 AND COUNT(ecp.eccp_ictipopagamento)::FLOAT/COUNT(ecc.ecco_id) < 1\n" +
                        "ORDER BY RANDOM() \n" +
                        "--LIMIT 10";
    
    public static final String QUERY_SELECT_COBRANCA_SET_PAGO_N_PAGO = "SELECT \n" +
"	ecc.ecco_dtenvioconta AS \"DATA ENVIO PARA COBRANCA\",\n" +
"	ecc.imov_id AS \"IMOVEL\",\n" +
"	CASE cli.clie_iccpfcnpjvalidado \n" +
"		WHEN 1 THEN 0.8 \n" +
"		ELSE 0.2\n" +
"	END AS \"DOC VALIDADO\",\n" +
"	imo.iper_id/12.0 AS \"PERFIL\",\n" +
"	imo.imov_idcategoriaprincipal/6.0 AS \"CATEGORIA PRINCIPAL\",\n" +
"	ftb.ftab_id/8.0 AS \"TIPO DO ABASTECIMENTO\",\n" +
"	imo.loca_id/1100.0 AS \"ID LOCALIDADE\",\n" +
"	imo.last_id/12.0 AS \"ID SIT. AGUA\",\n" +
"	imo.lest_id/12.0 AS \"ID SIT. ESG\",\n" +
"	CASE\n" +
"		WHEN NOT hid.hidr_nnhidrometro IS NULL THEN 0.8\n" +
"		ELSE 0.2 \n" +
"	END AS \"HIDROMETRO\",\n" +
"	COUNT(ecc.ecco_id) AS \"CONTAS ENVIADAS\",\n" +
"	COUNT(ecp.eccp_ictipopagamento) AS \"CONTAS PAGAS\",\n" +
"	CASE\n" +
"		WHEN NOT ecp.eccp_ictipopagamento IS NULL THEN 1\n" +
"		ELSE 0\n" +
"	END AS \"HOUVE PAGAMENTO\"\n" +
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
"	ecc.ecco_dtenvioconta >= VAR_DATE\n" +
"	AND NOT ecc.cnta_id IS NULL\n" +
"GROUP BY 1,2,3,4,5,6,7,8,9,10,13\n" +
"ORDER BY RANDOM() \n" +
"--LIMIT 10";
    
    private static final String[] COBRANCA_DATES= {
                                            //"2020-09-02",
                                            //"2020-10-08",
                                            //"2019-11-13",
        
                                            "'2019-11-14'",
                                            "'2019-10-23'",
                                            "'2019-07-26'",
                                            "'2021-01-05'",
                                            "'2019-10-01'",
                                            "'2020-02-13'",
                                            "'2019-10-22'",
                                            "'2020-01-08'",
                                            "'2019-07-18'",
                                            
                                            //"2020-10-14",
                                            //"2019-04-30",
                                            //"2019-05-01"
                                            };
    
    public ArrayList<ForexHistoryEntry> getDataSet(Connection con)
    {
        ArrayList<ForexHistoryEntry>  trainingSet =  new ArrayList<>();
        String query=TRAINING_DATA_QUERY;
        if(con==null)
            return trainingSet;
        try
        {
            try (ResultSet resultSet = con.prepareStatement(query).executeQuery()) {
                if(resultSet.next())
                {
                    do
                    {
                        ForexHistoryEntry forexHistoryEntry;
                        forexHistoryEntry = new ForexHistoryEntry(resultSet.getInt(7), resultSet.getDate(1), resultSet.getDouble(2), resultSet.getDouble(3), resultSet.getDouble(4), resultSet.getDouble(5), resultSet.getDouble(6));
                        trainingSet.add(forexHistoryEntry);
                    }while(resultSet.next());
                }
            }
        }catch(SQLException e)
        {
            System.err.println(e.getMessage());
        }
        return trainingSet;
    }
    
    public List<CobrancaPOJO> nextCobrancaDataSet() throws SQLException
    {
        List<CobrancaPOJO>  trainingSet =  Collections.synchronizedList(new ArrayList<>());
        Connection con = conectarDB(DB_DRIVER, DB_URL, DB_USER, DB_PASS);
        
//        String replacement = "";
//        for(int i = 0; i<COBRANCA_DATES.length; i++)
//        {
//            replacement+=COBRANCA_DATES[i]+",";
//        }
//        replacement = replacement.substring(0, replacement.length()-1);
//        String query=QUERY_SELECT_COBRANCA_SET.replaceAll("VAR_DATE", replacement);
        String query=QUERY_SELECT_COBRANCA_SET_4.replaceAll("VAR_DATE", COBRANCA_DATES[(int)(Math.random()*COBRANCA_DATES.length)]);
        ResultSet resultSet = con.createStatement().executeQuery(query);
        while(resultSet.next())
        {
            CobrancaPOJO cobrancaPOJO;
            cobrancaPOJO = new CobrancaPOJO(resultSet.getDate(1), resultSet.getInt(2), resultSet.getFloat(3), resultSet.getFloat(4), resultSet.getFloat(5), resultSet.getFloat(6), resultSet.getFloat(7), resultSet.getFloat(8), resultSet.getFloat(9), resultSet.getFloat(10), resultSet.getFloat(11), resultSet.getInt(12), resultSet.getFloat(13));
            trainingSet.add(cobrancaPOJO);
        }
        con.close();
        return trainingSet;
    }
    
    public void trainCobranca(NNManagerListener callback, long totalEpochs, long iterationsPerSet, String netName, Double untilMse) throws SQLException, IOException
    {
        final NeuralNetwork bpnn = new NeuralNetwork(-0.00001);
        int sizes[]={8,20,10,1};
        bpnn.createNetwork(new SigmoidActivator(), sizes);
        bpnn.setErrorFunction(new OneHalfMeanSquaredError());
        
        
        long iterations=0;
        long totalIterations=0;
        double summedTotalError = 0;
        long sizeTotal= 0;
        long backPropagations = 0;
        double correct=0;
        while(iterations<totalEpochs)
        {
            iterations++;
            
            ArrayList<CobrancaPOJO> trainingSet = new ArrayList<>(nextCobrancaDataSet());

            OneHalfMeanSquaredError omserpEpoch = new OneHalfMeanSquaredError();
            omserpEpoch.reset();
            double epochSumError=0;
            long subIteration = 0;
            for(int i = 0; i < iterationsPerSet; i++){
                int trainingSetIteration = 0;
                for (CobrancaPOJO cobrancaPOJO : trainingSet) {
                    double outputNN = bpnn.feedForward(cobrancaPOJO.getDocValid(),cobrancaPOJO.getPerfil(),
                            cobrancaPOJO.getCategoria(),cobrancaPOJO.getAbastecimento()
                            //,cobrancaPOJO.getLocalidade()
                            ,cobrancaPOJO.getSitAgua(), cobrancaPOJO.getSitEsg(),
                            cobrancaPOJO.getHd(),cobrancaPOJO.getEnviadas()).get(0);
                    omserpEpoch.acumulateError(cobrancaPOJO.getPropPag(), outputNN);
                    double out=0;
                    if(outputNN>0.5)
                        out = 1;
                    else
                        out = 0;
                    if((cobrancaPOJO.getPropPag()-out)==0)
                        correct++;
                    bpnn.getErrorFunction().acumulateError(cobrancaPOJO.getPropPag(), outputNN);
                    bpnn.calculateGradientsWithErrorFuncion();
                    bpnn.updateBackPropagationWeights(0.1, 0.00001);
                    backPropagations++;
                    subIteration++;
                    totalIterations++;
                    trainingSetIteration++;
                    sizeTotal++;
                    if(untilMse!=null)
                    {
                        if(untilMse>= (double)epochSumError/(double)subIteration)
                            iterations = totalEpochs;
                        else if(iterations==totalEpochs-1)
                            iterations = 0;
                    }
                    if(((float)subIteration*100/(float)trainingSet.size())%5==0)
                    {
                        epochSumError += omserpEpoch.getSumedError();
                        omserpEpoch.reset();
                        callback.onUpdate(iterations, totalEpochs, subIteration, trainingSet.size()*iterationsPerSet, 
                                (summedTotalError+epochSumError)/sizeTotal, Math.sqrt((summedTotalError+epochSumError)/sizeTotal), 
                                epochSumError/subIteration, Math.sqrt(epochSumError/subIteration), totalIterations, backPropagations, 
                                outputNN, cobrancaPOJO.getPropPag(), correct*100/totalIterations);
                    }
                }
            }
            summedTotalError += epochSumError;
            if(iterations%10==0){
                File tempF = new File(netName+"NNTemp.json");
                if(tempF.exists())
                    tempF.delete();
                tempF.createNewFile();
                bpnn.saveNNToFile(tempF);
            }
        }
        System.out.println("Training Ended");
        File tempF = new File(netName+"NNTemp.json");
        if(tempF.exists())
            tempF.delete();
        File nnFile = new File(netName+"NNF"+new Date().getTime()+".json");
        bpnn.saveNNToFile(nnFile);
        evaluateCobranca(new File("evaluate.csv"), bpnn);
    }
    
    
    public void evaluateCobranca(File data, File nnJson) throws IOException
    {
        final NeuralNetwork rpnn = new NeuralNetwork();
        rpnn.createNetwork(nnJson);
        
        evaluateCobranca(data, rpnn);
    }
    
    public void evaluateCobranca(File data, NeuralNetwork rpnn) throws IOException
    {
        
        RandomAccessFile rf = new RandomAccessFile(data, "rw");
        File fOut = new File("Result"+new Date().getTime()+".csv");
        OutputStreamWriter fos = new OutputStreamWriter(new FileOutputStream(fOut), "UTF-8");
        rf.seek(0);
        String headerLine = rf.readLine()+";NNOutput\n";
        fos.write(headerLine);
        String line;
        while((line = rf.readLine())!=null)
        {
            
            String fields[] = line.split(";");
            CobrancaPOJO cobrancaPOJO = new CobrancaPOJO();
            cobrancaPOJO.setDocValid(Float.parseFloat(fields[1]));
            cobrancaPOJO.setPerfil(Float.parseFloat(fields[2]));
            cobrancaPOJO.setCategoria(Float.parseFloat(fields[3]));
            cobrancaPOJO.setAbastecimento(Float.parseFloat(fields[4]));
            cobrancaPOJO.setLocalidade(Float.parseFloat(fields[5]));
            cobrancaPOJO.setSitAgua(Float.parseFloat(fields[6]));
            cobrancaPOJO.setSitEsg(Float.parseFloat(fields[7]));
            cobrancaPOJO.setHd(Float.parseFloat(fields[8]));
            cobrancaPOJO.setEnviadas(Float.parseFloat(fields[9]));

            Double result = rpnn.feedForward(cobrancaPOJO.getDocValid(),cobrancaPOJO.getPerfil(),
                    cobrancaPOJO.getCategoria(),cobrancaPOJO.getAbastecimento()
                    //,cobrancaPOJO.getLocalidade()
                    ,cobrancaPOJO.getSitAgua(), cobrancaPOJO.getSitEsg(),
                    cobrancaPOJO.getHd(),cobrancaPOJO.getEnviadas()).get(0);
            
            line +=";"+ Double.toString(result)+"\n";
            fos.write(line);
        }
        fos.close();
        rf.close();
    }
    
    public synchronized Connection conectarDB(String driver, String url, String user, String pass) {
        Connection con;
        try
        {
            Class.forName(driver);
        } catch (ClassNotFoundException ex)
        {
            System.err.println(ex.getMessage());
            return null;
        }
        try
        {
            con = DriverManager.getConnection(url, user, pass);
        } catch (SQLException ex)
        {
            ex.printStackTrace();
            System.exit(0);
            return null;
        }
        return con;
    }
}
