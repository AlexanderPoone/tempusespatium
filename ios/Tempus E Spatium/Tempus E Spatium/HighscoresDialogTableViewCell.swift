//
//  HighscoresDialogTableViewCell.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 21/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit

class HighscoresDialogTableViewCell: UITableViewCell {

    @IBOutlet weak var mRankLbl: UILabel!
    @IBOutlet weak var mPlayerLbl: UILabel!
    @IBOutlet weak var mScoreLbl: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
