package org.cocos2dx.lib

import android.content.Context
import android.content.res.Resources
import android.telephony.TelephonyManager
import com.blankj.utilcode.util.Utils

/**
 * <pre>
 * author: Blankj
 * blog  : http://blankj.com
 * time  : 2019/06/11
 * desc  : utils about country code
</pre> *
 */
object CountryUtils {

    private val countryCodeMap: Map<String, String> by lazy {
        mapOf(
            "AL" to "+355", "DZ" to "+213", "AF" to "+93", "AR" to "+54", "AE" to "+971",
            "AW" to "+297", "OM" to "+968", "AZ" to "+994", "AC" to "+247", "EG" to "+20",
            "ET" to "+251", "IE" to "+353", "EE" to "+372", "AD" to "+376", "AO" to "+244",
            "AI" to "+1", "AG" to "+1", "AT" to "+43", "AX" to "+358", "AU" to "+61",
            "BB" to "+1", "PG" to "+675", "BS" to "+1", "PK" to "+92", "PY" to "+595",
            "PS" to "+970", "BH" to "+973", "PA" to "+507", "BR" to "+55", "BY" to "+375",
            "BM" to "+1", "BG" to "+359", "MP" to "+1", "BJ" to "+229", "BE" to "+32",
            "IS" to "+354", "PR" to "+1", "PL" to "+48", "BA" to "+387", "BO" to "+591",
            "BZ" to "+501", "BW" to "+267", "BT" to "+975", "BF" to "+226", "BI" to "+257",
            "KP" to "+850", "GQ" to "+240", "DK" to "+45", "DE" to "+49", "TL" to "+670",
            "TG" to "+228", "DO" to "+1", "DM" to "+1", "RU" to "+7", "EC" to "+593",
            "ER" to "+291", "FR" to "+33", "FO" to "+298", "PF" to "+689", "GF" to "+594",
            "VA" to "+39", "PH" to "+63", "FJ" to "+679", "FI" to "+358", "CV" to "+238",
            "FK" to "+500", "GM" to "+220", "CG" to "+242", "CD" to "+243", "CO" to "+57",
            "CR" to "+506", "GG" to "+44", "GD" to "+1", "GL" to "+299", "GE" to "+995",
            "CU" to "+53", "GP" to "+590", "GU" to "+1", "GY" to "+592", "KZ" to "+7",
            "HT" to "+509", "KR" to "+82", "NL" to "+31", "BQ" to "+599", "SX" to "+1",
            "ME" to "+382", "HN" to "+504", "KI" to "+686", "DJ" to "+253", "KG" to "+996",
            "GN" to "+224", "GW" to "+245", "CA" to "+1", "GH" to "+233", "GA" to "+241",
            "KH" to "+855", "CZ" to "+420", "ZW" to "+263", "CM" to "+237", "QA" to "+974",
            "KY" to "+1", "CC" to "+61", "KM" to "+269", "XK" to "+383", "CI" to "+225",
            "KW" to "+965", "HR" to "+385", "KE" to "+254", "CK" to "+682", "CW" to "+599",
            "LV" to "+371", "LS" to "+266", "LA" to "+856", "LB" to "+961", "LT" to "+370",
            "LR" to "+231", "LY" to "+218", "LI" to "+423", "RE" to "+262", "LU" to "+352",
            "RW" to "+250", "RO" to "+40", "MG" to "+261", "IM" to "+44", "MV" to "+960",
            "MT" to "+356", "MW" to "+265", "MY" to "+60", "ML" to "+223", "MK" to "+389",
            "MH" to "+692", "MQ" to "+596", "YT" to "+262", "MU" to "+230", "MR" to "+222",
            "US" to "+1", "AS" to "+1", "VI" to "+1", "MN" to "+976", "MS" to "+1",
            "BD" to "+880", "PE" to "+51", "FM" to "+691", "MM" to "+95", "MD" to "+373",
            "MA" to "+212", "MC" to "+377", "MZ" to "+258", "MX" to "+52", "NA" to "+264",
            "ZA" to "+27", "SS" to "+211", "NR" to "+674", "NI" to "+505", "NP" to "+977",
            "NE" to "+227", "NG" to "+234", "NU" to "+683", "NO" to "+47", "NF" to "+672",
            "PW" to "+680", "PT" to "+351", "JP" to "+81", "SE" to "+46", "CH" to "+41",
            "SV" to "+503", "WS" to "+685", "RS" to "+381", "SL" to "+232", "SN" to "+221",
            "CY" to "+357", "SC" to "+248", "SA" to "+966", "BL" to "+590", "CX" to "+61",
            "ST" to "+239", "SH" to "+290", "PN" to "+870", "KN" to "+1", "LC" to "+1",
            "MF" to "+590", "SM" to "+378", "PM" to "+508", "VC" to "+1", "LK" to "+94",
            "SK" to "+421", "SI" to "+386", "SJ" to "+47", "SZ" to "+268", "SD" to "+249",
            "SR" to "+597", "SB" to "+677", "SO" to "+252", "TJ" to "+992", "TH" to "+66",
            "TZ" to "+255", "TO" to "+676", "TC" to "+1", "TA" to "+290", "TT" to "+1",
            "TN" to "+216", "TV" to "+688", "TR" to "+90", "TM" to "+993", "TK" to "+690",
            "WF" to "+681", "VU" to "+678", "GT" to "+502", "VE" to "+58", "BN" to "+673",
            "UG" to "+256", "UA" to "+380", "UY" to "+598", "UZ" to "+998", "GR" to "+30",
            "ES" to "+34", "EH" to "+212", "SG" to "+65", "NC" to "+687", "NZ" to "+64",
            "HU" to "+36", "SY" to "+963", "JM" to "+1", "AM" to "+374", "YE" to "+967",
            "IQ" to "+964", "UM" to "+1", "IR" to "+98", "IL" to "+972", "IT" to "+39",
            "IN" to "+91", "ID" to "+62", "GB" to "+44", "VG" to "+1", "IO" to "+246",
            "JO" to "+962", "VN" to "+84", "ZM" to "+260", "JE" to "+44", "TD" to "+235",
            "GI" to "+350", "CL" to "+56", "CF" to "+236", "CN" to "+86", "MO" to "+853",
            "TW" to "+886", "HK" to "+852"
        )
    }

    /**
     * Return the country code by sim card.
     *
     * @param defaultValue The default value.
     * @return the country code
     */
    fun getCountryCodeBySim(defaultValue: String): String {
        val code = countryCodeMap[getCountryBySim()]
        return code ?: defaultValue
    }

    /**
     * Return the country code by system language.
     *
     * @param defaultValue The default value.
     * @return the country code
     */
    fun getCountryCodeByLanguage(defaultValue: String): String {
        val code = countryCodeMap[getCountryByLanguage()]
        return code ?: defaultValue
    }

    /**
     * Return the country by sim card.
     *
     * @return the country
     */
    fun getCountryBySim(): String {
        val manager =
            Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
        return manager?.simCountryIso?.toUpperCase() ?: ""
    }

    /**
     * Return the country by system language.
     *
     * @return the country
     */
    fun getCountryByLanguage(): String {
        return Resources.getSystem().configuration.locale.country
    }
}
