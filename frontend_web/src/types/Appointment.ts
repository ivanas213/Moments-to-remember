import { Offer } from "./Offer";
import { User } from "./User";

export interface Appointment{
    _id: string,
    status: string,
    date: string,
    offer: Offer,
    createdAt: string |null,
    updatedAt: string | null,
    user: User,
    guests: number,
    new: boolean
}
