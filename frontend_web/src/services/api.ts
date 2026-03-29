import axios from "axios";
import { Config } from "@/constants/config";
import { User } from "@/types/User";
import { Promotion } from "@/types/Promotion";
import { Offer } from "@/types/Offer";
import { Appointment } from "@/types/Appointment";
import { AppointmentRequest } from "@/types/AppointmentRequest";
import { Rating } from "@/types/Rating";
import { Event } from "@/types/Event";
import { AddRatingRequest } from "@/types/AddRatingRequest";
import { setupCache } from "axios-cache-interceptor";
import { AppointRequest } from "@/types/AppointRequest";
import { Hall } from "@/types/Hall";
import { AddOfferRequest } from "@/types/AddOfferRequest";

const api = setupCache(
  axios.create({
    baseURL: Config.BASE_URL
  }),
  {
    ttl: 1000 * 60 * 5, 
  }
);

export const UserAPI = {
  login: (data: Record<string, string> ) => api.post<User>(`${Config.ROUTES.user}/login`, data),
  updateData: (userId: string, data: Record<string, string>) =>
    api.put<User>(`${Config.ROUTES.user}/updateData/${userId}`, data),
};

export const PromotionAPI = {
  getPromotions: () => api.get<Promotion[]>(`${Config.ROUTES.promotion}/getAll`)
};

export const EventAPI = {
  getEvents: () => api.get<Event[]>(`${Config.ROUTES.event}/getAll`)
};

export const OfferAPI = {
  getOffers: () => api.get<Offer[]>(`${Config.ROUTES.offer}/getAll`), 
  add: (req: AddOfferRequest) => {
    const fd = new FormData();
    fd.append("image", req.image);                     
    fd.append("name", req.name);
    fd.append("price", String(req.price));
    fd.append("minGuests", String(req.minGuests));
    fd.append("maxGuests", String(req.maxGuests));
    fd.append("hall", req.hall);
    return api.post(`${Config.ROUTES.offer}/add`, fd);
  }
};
export const AppointmentAPI = {
  addToCart: (request: AppointmentRequest) => api.post<Appointment>(`${Config.ROUTES.appointment}/add`, request),
  getCart: (userId: string) => api.get<Appointment[]>(`${Config.ROUTES.appointment}/getCart/${userId}`),
  deleteAppointment: (appointmentId: string) => api.delete(`${Config.ROUTES.appointment}/delete/${appointmentId}`),
  appoint: (request: AppointRequest) => api.post<Appointment[]>(`${Config.ROUTES.appointment}/appoint`, request),
  getPending: () => api.get(`${Config.ROUTES.appointment}/getPending`),
  accept: (id: string) => api.post(`${Config.ROUTES.appointment}/accept/${id}`),
  reject: (id: string) => api.post(`${Config.ROUTES.appointment}/reject/${id}`),
  notifications: (id: string) => api.get(`${Config.ROUTES.appointment}/getNotifications/${id}`)
};

export const RatingAPI = {
  addRating: (request: AddRatingRequest) => api.post<Rating>(`${Config.ROUTES.rating}/add`, request),
  getAllRatings: (offerId: string) => api.get<Rating[]>(`${Config.ROUTES.rating}/all/${offerId}`)
};


export const HallAPI = {
  getAllHalls: () => api.get<Hall[]>(`${Config.ROUTES.hall}/getAll`)
};



