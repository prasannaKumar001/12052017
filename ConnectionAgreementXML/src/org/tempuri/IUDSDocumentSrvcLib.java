package org.tempuri;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.2.0
 * 2017-11-12T09:49:52.143+03:00
 * Generated source version: 3.2.0
 * 
 */
@WebService(targetNamespace = "http://tempuri.org/", name = "IUDSDocumentSrvcLib")
@XmlSeeAlso({com.microsoft.schemas._2003._10.serialization.ObjectFactory.class, ObjectFactory.class, org.datacontract.schemas._2004._07.udsdocument.ObjectFactory.class, org.datacontract.schemas._2004._07.ObjectFactory.class})
public interface IUDSDocumentSrvcLib {

    @WebMethod(action = "http://tempuri.org/IUDSDocumentSrvcLib/getConnectionAgreement")
    @Action(input = "http://tempuri.org/IUDSDocumentSrvcLib/getConnectionAgreement", output = "http://tempuri.org/IUDSDocumentSrvcLib/getConnectionAgreementResponse", fault = {@FaultAction(className = IUDSDocumentSrvcLibGetConnectionAgreementServiceResponseFaultFaultMessage.class, value = "http://tempuri.org/IUDSDocumentSrvcLib/getConnectionAgreementServiceResponseFault")})
    @RequestWrapper(localName = "getConnectionAgreement", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetConnectionAgreement")
    @ResponseWrapper(localName = "getConnectionAgreementResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetConnectionAgreementResponse")
    @WebResult(name = "getConnectionAgreementResult", targetNamespace = "http://tempuri.org/")
    public org.datacontract.schemas._2004._07.udsdocument.ConnectionAgreement getConnectionAgreement(
        @WebParam(name = "ReqID", targetNamespace = "http://tempuri.org/")
        java.lang.Integer reqID
    ) throws IUDSDocumentSrvcLibGetConnectionAgreementServiceResponseFaultFaultMessage;
}
