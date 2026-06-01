import api from "./api";
import * as Person from "../models/person.model";

export async function getPersonById(id) {
    const response = await api.get(`/api/persons/${id}`);
    return { id, ...Person.fromApi(response.data) };
}
