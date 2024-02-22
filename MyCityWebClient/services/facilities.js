import authHeader, { BASE_URL, HTTP } from "./http";

export const facilities = {
    findAll() {
        return HTTP.get(BASE_URL + "/admin/facilities",{ headers: authHeader() } ).then(
            (response) => {
                return response.data;
            }
        );
    },
    findByType(type){
        return HTTP.get(BASE_URL + "/admin/facilities/"+type,{ headers: authHeader() } ).then(
            (response) => {
                return response.data;
            }
        );
    },
    create(facility) {
        return HTTP.post(BASE_URL + "/admin/facilities", facility, { headers: authHeader() }).then(
            (response) => {
                return response.data;
            }
        );
    },
    update(facility) {
        return HTTP.put(BASE_URL + "/admin/facilities" , facility, {
            headers: authHeader(),
        }).then((response) => {
            return response.data;
        });
    },
    delete(id) {
        return HTTP.delete(BASE_URL + "/admin/facilities/"+ id, {
            headers: authHeader(),
        }).then((response) => {
            return response.data;
        });
    },

};