package zw.co.dreamhub.weekend_fun.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Marlvin Chihota
 * Email marlvinchihota@gmail.com
 * Created on 21/6/2024
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {

    @JsonProperty("product_id")
    private int productId;

    private String text;
    private Translations translations;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Translations {

        @JsonProperty("RU")
        private String rU;

        @JsonProperty("UK")
        private String uK;

        @JsonProperty("BG")
        private String bG;

        @JsonProperty("HR")
        private String hR;

        @JsonProperty("CS")
        private String cS;

        @JsonProperty("DA")
        private String dA;

        @JsonProperty("NL")
        private String nL;

        @JsonProperty("EN")
        private String eN;

        @JsonProperty("ET")
        private String eT;

        @JsonProperty("FI")
        private String fI;

        @JsonProperty("FR")
        private String fR;

        @JsonProperty("DE")
        private String dE;

        @JsonProperty("EL")
        private String eL;

        @JsonProperty("HU")
        private String hU;

        @JsonProperty("GA")
        private String gA;

        @JsonProperty("IT")
        private String iT;

        @JsonProperty("LV")
        private String lV;

        @JsonProperty("LT")
        private String lT;

        @JsonProperty("MT")
        private String mT;

        @JsonProperty("PL")
        private String pL;

        @JsonProperty("PT")
        private String pT;

        @JsonProperty("RO")
        private String rO;

        @JsonProperty("SK")
        private String sK;

        @JsonProperty("SL")
        private String sL;

        @JsonProperty("ES")
        private String eS;

        @JsonProperty("SV")
        private String sV;

        @JsonProperty("JA")
        private String jA;

        @JsonProperty("ZH")
        private String zH;

        @JsonProperty("KO")
        private String kO;

        @JsonProperty("HI")
        private String hI;

        @JsonProperty("ID")
        private String iD;

        @JsonProperty("TR")
        private String tR;

        @JsonProperty("CA")
        private String cA;

        @JsonProperty("VI")
        private String vI;

        @JsonProperty("TH")
        private String tH;

        @JsonProperty("NO")
        private String nO;

        @JsonProperty("SR")
        private String sR;

        @JsonProperty("MS")
        private String mS;

        @JsonProperty("BN")
        private String bN;

        @JsonProperty("SW")
        private String sW;
    }

}
