package de.viadee.ki.ipv;

import java.net.URI;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import de.viadee.ki.ipv.config.Config;
import de.viadee.ki.ipv.model.ProcessConstants;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Config.class })
public class KFZSchadenApplicationStartProcessIT {

    @Autowired
    RestTemplate restTemplate;

  
   
    @Test
    public void kfzSchadenMelden_Anomalie_IT() throws Exception {
        JSONObject claim1 = createClaimdataAsJSON(5, 1000, 5515689f, "1", 8, 8, "Reperatur", 0);
        sendClaim2Process(claim1);
    }
    @Test
    public void kfzSchadenMelden_KlassifikationHandlung_IT() throws Exception {
        JSONObject claim1 = createClaimdataAsJSON(5, 2010, 15689f, "1", 1, 5, "Reperatur", 0);
        sendClaim2Process(claim1);
    }

    @Test
    public void kfzSchadenMelden_Klassifikation_IT() throws Exception {
        JSONObject claim1 = createClaimdataAsJSON(3, 2015, 2587f, "2", 1, 5, "Reperatur", 0);
        sendClaim2Process(claim1);
    }











    private void sendClaim2Process(JSONObject personDataVariables) throws URISyntaxException, JSONException {
        URI url = new URI("http://localhost:8080/rest/process-definition/key/kfzSchadenbearbeitung/start");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject startProcessinstance = new JSONObject();
        startProcessinstance.put("variables", personDataVariables);

        HttpEntity<String> request = new HttpEntity<String>(startProcessinstance.toString(), headers);
        String response = restTemplate.postForObject(url, request, String.class);
        System.out.println("response: " + response);
    }

    private JSONObject createClaimdataAsJSON(int id, int year, double costs, String typeclass, int passengers, int doors, String repairtype,
    int rejected) throws JSONException {
        JSONObject idJO = new JSONObject();
        idJO.put("value", id + 10000);
        idJO.put("type", "Integer");
        JSONObject yearJO = new JSONObject();
        yearJO.put("value", year);
        yearJO.put("type", "Integer");
        JSONObject costsJO = new JSONObject();
        costsJO.put("value", costs);
        costsJO.put("type", "Double");
        JSONObject typeclassJO = new JSONObject();
        typeclassJO.put("value", typeclass);
        typeclassJO.put("type", "String");
        JSONObject passengersJO = new JSONObject();
        passengersJO.put("value", passengers);
        passengersJO.put("type", "Integer");
        JSONObject doorsJO = new JSONObject();
        doorsJO.put("value", doors);
        doorsJO.put("type", "Integer");
        
        JSONObject repairtypeJO = new JSONObject();
        repairtypeJO.put("value", repairtype);
        repairtypeJO.put("type", "String");
        
        JSONObject rejectedJO = new JSONObject();
        rejectedJO.put("value", rejected);
        rejectedJO.put("type", "Integer");

        JSONObject variables = new JSONObject();
        variables.put(ProcessConstants.INT_CLAIM_ID, idJO);
        variables.put(ProcessConstants.INT_CLAIM_YEAR, yearJO);
        variables.put(ProcessConstants.INT_CLAIM_COSTS, costsJO);
        variables.put(ProcessConstants.INT_CLAIM_TYPECLASS, typeclassJO);
        variables.put(ProcessConstants.INT_CLAIM_DOORS, doorsJO);
        variables.put(ProcessConstants.INT_CLAIM_PASSENGERS, passengersJO);
        variables.put(ProcessConstants.INT_CLAIM_REPAIRTYPE, repairtypeJO);
        variables.put(ProcessConstants.INT_CLAIM_REJECTED, rejectedJO);

        return variables;
    }

}
