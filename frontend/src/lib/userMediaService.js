import api from "./api";
import { MediaModel } from "../models";

export async function getRecommendations(userId) {
    const response = await api.get(`/api/user/${userId}/recommendations`);
    return MediaModel.fromApiList(response.data);
}

export async function getWatchlist(username) {
    const response = await api.get(`/api/user/${encodeURIComponent(username)}/watchlist`);
    return MediaModel.fromApiList(response.data);
}
