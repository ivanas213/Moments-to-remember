import { Hall } from "./Hall";

export interface Offer{
    _id: string,
    offerImageUrl: string,
    name: string,
    minGuests: number,
    maxGuests: number,
    price: number,
    hall: Hall
}