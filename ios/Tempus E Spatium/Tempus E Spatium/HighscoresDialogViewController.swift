//
//  HighscoresDialogViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 20/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit
import SwiftIcons

class HighscoresDialogViewController: UIViewController, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return mHighscores.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "lasciaChioPianga", for: indexPath) as! HighscoresDialogTableViewCell
        cell.mRankLbl.text = String(indexPath.row + 1)
        cell.mPlayerLbl.text = (mHighscores[indexPath.row][0] as! String)
        cell.mScoreLbl.text = String(mHighscores[indexPath.row][1] as! Int)
        return cell
    }
    
    private var mHighscores:[[Any]] = []
    
    @IBOutlet weak var mHighscoresLbl: UILabel!
    
    @IBOutlet weak var mClearBtn: UIButton!
    
    @IBAction func mClearHighscores() {
        HighscoresDBManager.shared.clearHighscores()
        mHighscores = []
        mHighscoresTableView.reloadData()
    }
    
    @IBOutlet weak var mHighscoresTableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        mHighscores = HighscoresDBManager.shared.getHighscores()

        mHighscoresTableView.dataSource = self
        
        mHighscoresLbl!.setIcon(prefixText: "", prefixTextFont: mHighscoresLbl.font!, prefixTextColor: UIColor(named: "Sienna")!, icon: .fontAwesomeSolid(.trophy), iconColor: UIColor(named: "Sienna")!, postfixText: NSLocalizedString("highscores", comment: ""), postfixTextFont: mHighscoresLbl.font!, postfixTextColor: UIColor(named: "Sienna")!, iconSize: nil)
        
        mClearBtn.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.undo), iconColor: .white, postfixText: NSLocalizedString("reset", comment: ""), postfixTextColor: .white, backgroundColor: UIColor(named: "danger")!, forState: .normal, textSize: nil, iconSize: nil)
        
        let width = UIScreen.main.bounds.size.width
        let height = UIScreen.main.bounds.size.height

        let imageViewBackground = UIImageView(frame: CGRect(x: 0, y: 0, width: width, height: height))
        imageViewBackground.image = UIImage(named: "board_background")

        imageViewBackground.contentMode = UIView.ContentMode.scaleAspectFill

        view.addSubview(imageViewBackground)
        view.sendSubviewToBack(imageViewBackground)
  
//        for var x in 0..<mHighscores.count {
//
//        }
        
        
        
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

