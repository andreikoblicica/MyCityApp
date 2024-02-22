import authHeader, { BASE_URL, HTTP } from "./http";

export const events = {
    findAll() {
        return HTTP.get(BASE_URL + "/admin/events",{ headers: authHeader() } ).then(
            (response) => {
                return response.data;
            }
        );
    },
    findAllTypes() {
        return HTTP.get(BASE_URL + "/admin/events/types",{ headers: authHeader() } ).then(
            (response) => {
                return response.data;
            }
        );
    },
    findByStatus(status) {
        return HTTP.get(BASE_URL + "/admin/events/"+status,{ headers: authHeader() } ).then(
            (response) => {
                return response.data;
            }
        );
    },

    create(event) {
        return HTTP.post(BASE_URL + "/admin/events", event, { headers: authHeader() }).then(
            (response) => {
                return response.data;
            }
        );
    },
    update(event) {
        return HTTP.put(BASE_URL + "/admin/events" , event, {
            headers: authHeader(),
        }).then((response) => {
            return response.data;
        });
    },
    updateStatus(event) {
        return HTTP.post(BASE_URL + "/admin/events/update-status" , event, {
            headers: authHeader(),
        }).then((response) => {
            return response.data;
        });
    },
    delete(id) {
        return HTTP.delete(BASE_URL + "/admin/events/"+ id, {
            headers: authHeader(),
        }).then((response) => {
            return response.data;
        });
    },

};