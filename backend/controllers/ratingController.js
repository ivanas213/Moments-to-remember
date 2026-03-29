import Rating from '../models/rating.js'
const add = async (req, res) => {
  try {
    const { user, value, comment, offer } = req.body;

    if (!user || !value || !comment || !offer) {
      return res.status(400).json({ message: "Sva polja su obavezna" });
    }

      let rating = await Rating.findOne({ user, offer });

      if (rating) {
        rating.value = value;
        rating.comment = comment;
        await rating.save();
      } else {
        rating = new Rating({ user, offer, value, comment });
        await rating.save();
      }

      await rating.populate([
        { path: "user" },
        { path: "offer", populate: { path: "hall" } }
      ]);

      return res.status(201).json(rating);
    } catch (error) {
      console.log(error.message);
      res.status(500).json({ error: error.message });
    }
}

const getAll = async (req, res) => {
    try{
        const offerId = req.params.offerId
        const ratings = await Rating.find({offer: offerId}).populate({ path: 'user' }).populate({
      path: 'offer',
      populate: { path: 'hall' }
    })
        res.status(200).json(ratings)
    }
    catch (error) {
        console.log(error.message);
        res.status(500).json({ error: error.message });
    }
}

export {add, getAll}