import Offer from '../models/offer.js'
import { uploadOffer } from '../utils/storage.js'

const upload = (req,res)=>{
    uploadOffer.single('image')(req, res, async function (err){
    if (err) {
      console.error(' Multer greška:', err);
      return res.status(500).json({
        error: 'Greška u upload middleware-u',
        message: err.message,
        stack: err.stack
      });
    }
     try {
        const offer = new Offer({
            offerImageUrl: req.file.path,
            minGuests: req.body.minGuests,
            maxGuests: req.body.maxGuests,
            price: req.body.price,
            hall: req.body.hall,
            name: req.body.name
        });

        await offer.save();

        res.json({ message: 'Uspešno!', offer });
    } catch (saveError) {
      console.error(' Greška pri čuvanju u bazi:', saveError);
      res.status(500).json({
        error: saveError.message,
        stack: saveError.stack
      });
    }
})
}
const add = async (req, res) => {
  try {
    if (!req.file) {
        console.log("Nema fajla")
      return res.status(400).json({ message: "Nije poslata slika (field 'image')." });
    }

    const { minGuests, maxGuests, price, hall, name } = req.body;

    if (!name || !price || !minGuests || !maxGuests || !hall) {
      return res.status(400).json({ message: "Sva polja su obavezna." });
    }

    const _min = Number(minGuests);
    const _max = Number(maxGuests);
    const _price = Number(price);
    if (Number.isNaN(_min) || Number.isNaN(_max) || Number.isNaN(_price)) {
      return res.status(400).json({ message: "Neispravne numeričke vrednosti." });
    }

    const offerImageUrl = req.file.path || req.file.location || req.file.url;

    const offer = new Offer({
      offerImageUrl,
      minGuests: _min,
      maxGuests: _max,
      price: _price,
      hall,  
      name,
    });

    await offer.save();
    await offer.populate({ path: "hall" });

    return res.status(201).json({ message: "Uspešno!", offer });
  } catch (error) {
    console.error("Greška pri čuvanju ponude:", error);
    return res.status(500).json({ message: error.message });
  }
};

const getAll = async (req, res) => {
    try {
        const offers = await Offer.find().populate('hall');
        res.json(offers);
    } catch (error) {
        console.log(error.message);
        res.status(500).json({ error: error.message });
    }
}


export {upload, getAll, add}