//
//  HighscoresDialogViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 20/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit
import SwiftIcons

class HighscoresDialogViewController: UIViewController {
    
    @IBOutlet weak var mHighscoresLbl: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        mHighscoresLbl!.setIcon(prefixText: "", prefixTextFont: mHighscoresLbl.font!, prefixTextColor: UIColor(named: "Sienna")!, icon: .fontAwesomeSolid(.trophy), iconColor: UIColor(named: "Sienna")!, postfixText: NSLocalizedString("highscores", comment: ""), postfixTextFont: mHighscoresLbl.font!, postfixTextColor: UIColor(named: "Sienna")!, iconSize: nil)
        
        let width = UIScreen.main.bounds.size.width
        let height = UIScreen.main.bounds.size.height

        let imageViewBackground = UIImageView(frame: CGRect(x: 0, y: 0, width: width, height: height))
        imageViewBackground.image = UIImage(named: "board_background")

        imageViewBackground.contentMode = UIView.ContentMode.scaleAspectFill

        view.addSubview(imageViewBackground)
        view.sendSubviewToBack(imageViewBackground)
        
        
//        let backgroundImageView = UIImageView(frame: UIScreen.main.bounds)
//        backgroundImageView.image = UIImage(named: "board_background")
//        backgroundImageView.contentMode = .scaleToFill
//        backgroundImageView.translatesAutoresizingMaskIntoConstraints = false
//
//        view.addSubview(backgroundImageView)
//        view.sendSubviewToBack(backgroundImageView)
//
//        // adding NSLayoutConstraints
//        let leadingConstraint = NSLayoutConstraint(item: backgroundImageView, attribute: .leading, relatedBy: .equal, toItem: self, attribute: .leading, multiplier: 1.0, constant: 0.0)
//        let trailingConstraint = NSLayoutConstraint(item: backgroundImageView, attribute: .trailing, relatedBy: .equal, toItem: self, attribute: .trailing, multiplier: 1.0, constant: 0.0)
//        let topConstraint = NSLayoutConstraint(item: backgroundImageView, attribute: .top, relatedBy: .equal, toItem: self, attribute: .top, multiplier: 1.0, constant: 0.0)
//        let bottomConstraint = NSLayoutConstraint(item: backgroundImageView, attribute: .bottom, relatedBy: .equal, toItem: self, attribute: .bottom, multiplier: 1.0, constant: 0.0)
//
//        NSLayoutConstraint.activate([leadingConstraint, trailingConstraint, topConstraint, bottomConstraint])
    }
}

