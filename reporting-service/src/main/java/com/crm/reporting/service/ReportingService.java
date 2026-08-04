package com.crm.reporting.service;

import com.crm.reporting.client.CrmServiceClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportingService {

    private final CrmServiceClient crmServiceClient;

    public ReportingService(CrmServiceClient crmServiceClient) {
        this.crmServiceClient = crmServiceClient;
    }

    public Map<String, Object> summary(String bearerToken) {
        List<Map<String, Object>> customers = crmServiceClient.getCustomers(bearerToken);
        List<Map<String, Object>> leads = crmServiceClient.getLeads(bearerToken);
        List<Map<String, Object>> deals = crmServiceClient.getDeals(bearerToken);

        Map<String, Long> leadsByStatus = new HashMap<>();
        for (Map<String, Object> lead : leads) {
            String status = String.valueOf(lead.get("status"));
            leadsByStatus.merge(status, 1L, Long::sum);
        }

        Map<String, BigDecimal> pipelineByStage = new HashMap<>();
        BigDecimal openPipelineValue = BigDecimal.ZERO;
        for (Map<String, Object> deal : deals) {
            String stage = String.valueOf(deal.get("stage"));
            BigDecimal amount = new BigDecimal(String.valueOf(deal.get("amount")));
            pipelineByStage.merge(stage, amount, BigDecimal::add);
            if (!"WON".equals(stage) && !"LOST".equals(stage)) {
                openPipelineValue = openPipelineValue.add(amount);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("customerCount", customers.size());
        result.put("leadCount", leads.size());
        result.put("dealCount", deals.size());
        result.put("leadsByStatus", leadsByStatus);
        result.put("pipelineValueByStage", pipelineByStage);
        result.put("openPipelineValue", openPipelineValue);
        return result;
    }
}
