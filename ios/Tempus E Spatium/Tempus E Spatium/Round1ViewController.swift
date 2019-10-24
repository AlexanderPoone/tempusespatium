//
//  Round1ViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 6/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit
import SwiftIcons

class Round1ViewController: UIViewController {

    @IBOutlet weak var mPlayer1Container: UIView!
    
    var mSecs:CGFloat = 0, mMaxTime:CGFloat = 0
    
    var mPlayer1:ExteriorViewController?
        var mPlayer2:ExteriorViewController?
    var mTimer:Timer?
        
    @IBAction func unwindToRound1ViewController(segue: UIStoryboardSegue) {
    }
    
    @objc func updateCounter() {
            mPlayer1!.mDonutTime!.progress = mSecs / mMaxTime
            mPlayer2!.mDonutTime!.progress = mSecs / mMaxTime
            mPlayer1!.mDonutTime!.progressLabel.text = String(Int(mSecs))
            mPlayer2!.mDonutTime!.progressLabel.text = String(Int(mSecs))
            mSecs -= 1
        if mSecs < 0 {
            mTimer!.invalidate()
            //reveal
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        mPlayer1Container.transform =  CGAffineTransform(rotationAngle: .pi)
        
        mPlayer1 = (children.first! as! ExteriorViewController)
        mPlayer2 = (children.last! as! ExteriorViewController)
        mPlayer1!.view!.backgroundColor = UIColor(named: "CosmicLatte")!
        
        mMaxTime = 60
        mSecs = 60
        mTimer = Timer.scheduledTimer(timeInterval: 1.0, target: self, selector: #selector(updateCounter), userInfo: nil, repeats: true)
        
        mPlayer1!.mScoreChangeLbl!.text = "+0"
        mPlayer1!.mScoreChangeLbl!.textColor = UIColor(named: "AndroidGreen")!
        
        mPlayer2!.mScoreChangeLbl!.text = "-10"
        mPlayer2!.mScoreChangeLbl!.textColor = UIColor(named: "Firebrick")!
    }
}
