export interface AddOfferRequest{
    image: File,
    name: string,
    minGuests: number,
    maxGuests: number,
    price: number,
    hall: string
}