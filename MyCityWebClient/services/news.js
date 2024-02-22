import authHeader, { BASE_URL, HTTP } from "./http";


export const news = {
    findAll() {
        return HTTP.get(BASE_URL + "/admin/news",{ headers: authHeader() } ).then(
            (response) => {
                return response.data;
            }
        );
    },
    findByInstitution(id) {
        return HTTP.get(BASE_URL + "/admin/news/"+id,{ headers: authHeader() } ).then(
            (response) => {
                return response.data;
            }
        );
    },
    create(newss) {
        return HTTP.post(BASE_URL + "/admin/news", newss, { headers: authHeader() }).then(
            (response) => {
                return response.data;
            }
        );
    },
    delete(id) {
        return HTTP.delete(BASE_URL + "/admin/news/"+ id, {
            headers: authHeader(),
        }).then((response) => {
            return response.data;
        });
    },

};