import authHeader, { BASE_URL, HTTP } from "./http";

export const alerts = {
    findAll() {
        return HTTP.get(BASE_URL + "/admin/alerts",{ headers: authHeader() } ).then(
            (response) => {
                return response.data;
            }
        );
    },
    findByInstitution(id) {
        return HTTP.get(BASE_URL + "/admin/alerts/"+id,{ headers: authHeader() } ).then(
            (response) => {
                return response.data;
            }
        );
    },
    create(alert) {
        return HTTP.post(BASE_URL + "/admin/alerts", alert, { headers: authHeader() }).then(
            (response) => {
                return response.data;
            }
        );
    },
    delete(id) {
        return HTTP.delete(BASE_URL + "/admin/alerts/"+ id, {
            headers: authHeader(),
        }).then((response) => {
            return response.data;
        });
    },

};