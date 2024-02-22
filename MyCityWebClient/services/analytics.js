import authHeader, { BASE_URL, HTTP } from "./http";

export const analytics = {
    getAnalytics() {
        return HTTP.get(BASE_URL + "/admin/analytics",{ headers: authHeader() } ).then(
            (response) => {
                return response.data;
            }
        );
    },

    getInstitutionAnalytics(id,name) {
        console.log(BASE_URL + "/institution/analytics/"+id+"/"+name)
        return HTTP.get(BASE_URL + "/institution/analytics/"+id+"/"+name,{ headers: authHeader() } ).then(
            (response) => {
                return response.data;
            }
        );
    },

};