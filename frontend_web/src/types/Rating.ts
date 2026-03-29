import { Offer } from "./Offer"
import { User } from "./User"

export interface Rating{
    _id: string,
    user: User,
    offer: Offer,
    value: number,
    comment: string
}