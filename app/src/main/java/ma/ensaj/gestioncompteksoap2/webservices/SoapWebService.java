package ma.ensaj.gestioncompteksoap2.webservices;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

//public class SoapWebService {
//    private static final String NAMESPACE = "http://ws.Comptes.example.com";
//    private static final String URL = "http://10.0.2.2:8082/services/ws";
//    private static final String TAG = "SoapWebService";
//
//    public static SoapObject getComptes() throws Exception {
//        try {
//            String methodName = "getComptes";
//            SoapObject request = new SoapObject(NAMESPACE, methodName);
//
//            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//            envelope.setOutputSoapObject(request);
//            envelope.dotNet = false;
//
//            HttpTransportSE transport = new HttpTransportSE(URL);
//
//            // Log request details
//            Log.d(TAG, "Request URL: " + URL);
//            Log.d(TAG, "Namespace: " + NAMESPACE);
//            Log.d(TAG, "Method Name: " + methodName);
//
//            transport.call(NAMESPACE + methodName, envelope);
//
//            Object response = envelope.getResponse();
//
//            // Log response details
//            Log.d(TAG, "Response Type: " + (response != null ? response.getClass().getName() : "null"));
//            Log.d(TAG, "Response: " + response);
//
//            if (response instanceof SoapObject) {
//                SoapObject soapResponse = (SoapObject) response;
//                Log.d(TAG, "Comptes récupérés : " + soapResponse.getPropertyCount());
//                return soapResponse;
//            } else {
//                Log.e(TAG, "Unexpected response type");
//                throw new Exception("Unexpected response type");
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "Erreur getComptes", e);
//            throw e;
//        }
//    }
//
//    // Method to add a new account
//    public static boolean addCompte(String solde, String type) throws Exception {
//        String methodName = "createCompte";
//        SoapObject request = new SoapObject(NAMESPACE, methodName);
//
//        request.addProperty("solde", solde);
//        request.addProperty("type", type);
//
//        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//        envelope.setOutputSoapObject(request);
//        envelope.dotNet = false;
//
//        HttpTransportSE transport = new HttpTransportSE(URL);
//        transport.call(NAMESPACE + methodName, envelope);
//
//        return Boolean.parseBoolean(envelope.getResponse().toString());
//    }
//
//    // Method to update an account
//    public static boolean updateCompte(int id, String solde, String type) throws Exception {
//        String methodName = "updateCompte";
//        SoapObject request = new SoapObject(NAMESPACE, methodName);
//
//        request.addProperty("id", id);
//        request.addProperty("solde", solde);
//        request.addProperty("type", type);
//
//        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//        envelope.setOutputSoapObject(request);
//        envelope.dotNet = false;
//
//        HttpTransportSE transport = new HttpTransportSE(URL);
//        transport.call(NAMESPACE + methodName, envelope);
//
//        return Boolean.parseBoolean(envelope.getResponse().toString());
//    }
//
//    // Method to delete an account
//    public static boolean deleteCompte(int id) throws Exception {
//        String methodName = "deleteCompte";
//        SoapObject request = new SoapObject(NAMESPACE, methodName);
//
//        request.addProperty("id", id);
//
//        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//        envelope.setOutputSoapObject(request);
//        envelope.dotNet = false;
//
//        HttpTransportSE transport = new HttpTransportSE(URL);
//        transport.call(NAMESPACE + methodName, envelope);
//
//        return Boolean.parseBoolean(envelope.getResponse().toString());
//    }
//}


import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;



import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;



import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import ma.ensaj.gestioncompteksoap2.beans.Compte;

public class SoapWebService  {
    // Ajout du slash à la fin du NAMESPACE
    private static final String NAMESPACE = "http://ws.GestionReservation.ensaj.ma";
    private static final String URL = "http://10.0.2.2:8082/services/ws";
    private static final String TAG = "SoapWebService";


    public static Object callWebService(String methodName, SoapObject request) throws Exception {

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        HttpTransportSE transport = new HttpTransportSE(URL);
        String soapAction = NAMESPACE + methodName;

        transport.call("", envelope);
        Log.d(TAG, "Request URL: " + URL);
        Log.d(TAG, "Namespace: " + NAMESPACE);

        return envelope.getResponse();
    }

    private Compte parseCompte(SoapObject soapObject) {
        return new Compte(
                Long.parseLong(soapObject.getProperty("id").toString()),
                Double.parseDouble(soapObject.getProperty("solde").toString()),
                soapObject.getProperty("dateCreation").toString(),
                soapObject.getProperty("type").toString()
        );
    }


    public  List<Compte> getComptes()  {
        List<Compte> comptes = new ArrayList<>();
        try {
            String methodName = "getComptes";
            SoapObject request = new SoapObject(NAMESPACE, methodName);

            Log.d(TAG, "Method Name: " + methodName);

            Object response = SoapWebService.callWebService(methodName,request);
            Log.d(TAG, "Response Type: " + (response != null ? response.getClass().getName() : "null"));
            Log.d(TAG, "Response: " + response);

            if (response instanceof Vector) {

                Vector<?> responseVector = (Vector<?>) response;
                for (Object item : responseVector) {
                    if (item instanceof SoapObject) {
                        comptes.add(parseCompte((SoapObject) item));
                    }
                }
            } else if (response instanceof SoapObject) {

                comptes.add(parseCompte((SoapObject) response));
            }
        } catch(Exception e) {
            e.printStackTrace();}

        return comptes;
    }


    // Les autres méthodes avec le même changement de NAMESPACE
    public  boolean createCompte(double solde, String type) {
        try {
            String methodName = "createCompte";


            SoapObject request = new SoapObject(NAMESPACE, methodName);

            Log.d(TAG, "Method Name: " + methodName);

            //request.addProperty("solde", solde);
            // Convertir le double en String
            request.addProperty("solde", String.valueOf(solde));
            request.addProperty("type", type);


            Object response = SoapWebService.callWebService(methodName,request);
            Log.d(TAG, "Response Type: " + (response != null ? response.getClass().getName() : "null"));
            Log.d(TAG, "Response: " + response);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Erreur addCompte", e);
            e.printStackTrace();
            return false;
        }
    }

    public  boolean deleteCompte(Long id)  {
        try {
            String methodName = "deleteCompte";
            SoapObject request = new SoapObject(NAMESPACE, methodName);
            request.addProperty("id", id);
            SoapWebService.callWebService(methodName, request);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Erreur deleteCompte", e);
           e.printStackTrace();
           return false;
        }
    }

    public  boolean updateCompte(Long id,double solde, String type)  {
        try {
            String methodName = "updateCompte";
            SoapObject request = new SoapObject(NAMESPACE, methodName);
            request.addProperty("id", String.valueOf(id));
            request.addProperty("solde", String.valueOf(solde));
           // request.addProperty("id",id );
            //request.addProperty("solde", solde);
            request.addProperty("type", type);


            SoapWebService.callWebService(methodName,request);

            return true;
        } catch (Exception e) {
            Log.e(TAG, "Erreur update Compte", e);
             e.printStackTrace();
             return false;
        }
    }


}