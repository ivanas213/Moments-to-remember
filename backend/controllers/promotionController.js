import Promotion from '../models/promotion.js';
import { uploadPromo } from '../utils/storage.js';

const upload = (req, res) => {
  uploadPromo.single('image')(req, res, async function (err) {
    if (err) {
      return res.status(500).json({
        error: 'Greška u upload middleware-u',
        message: err.message,
        stack: err.stack
      });
    }
    try {
      const promotion = new Promotion({
        imageUrl: req.file.path,
        description: req.body.description || '',
      });

      await promotion.save();

      res.json({ message: 'Uspešno!', promotion });
    } catch (saveError) {
      console.error(' Greška pri čuvanju u bazi:', saveError);
      res.status(500).json({
        error: saveError.message,
        stack: saveError.stack
      });
    }
  });
}
const getAll = async(req, res)=>{
    try{
        const promotions = await Promotion.find()
        res.json(promotions)
    }
    catch(error){
      console.log("Neuspeh"+ error.message)
      res.status(500).json({error:error.message})
    }
}

export {upload, getAll}