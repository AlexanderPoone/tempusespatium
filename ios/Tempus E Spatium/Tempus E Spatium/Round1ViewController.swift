//
//  Round1ViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 6/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit
import SwiftIcons
import Alamofire

class Round1ViewController: UIViewController {
    
    @IBOutlet weak var mPlayer1Container: UIView!
    
    var mSecs:CGFloat = 0, mMaxTime:CGFloat = 0
    var mCooldownSecs:CGFloat = 5
    
    var mPointsA, mPointsB
    
    var mArticles:[String:String]?
    
    var mPlayer1:ExteriorViewController?
    var mPlayer2:ExteriorViewController?
    var mTimer:Timer?
    var mCoolDownTimer:Timer?
    
    var mQuestionType:Int? = 5
    
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
            mTimer = nil
            reveal()
            mCooldownSecs = 5
            mCoolDownTimer = Timer.scheduledTimer(timeInterval: 1.0, target: self, selector: #selector(coolDown), userInfo: nil, repeats: true)
        }
    }
    
    @objc func coolDown() {
        mSecs -= 1
        if mSecs < 0 {
            mCoolDownTimer!.invalidate()
            mCoolDownTimer = nil
            replaceFragment()
        }
    }
    
    func reveal() {
        mPlayer1!.mDonutTime!.progress = 0
        mPlayer2!.mDonutTime!.progress = 0
        mPlayer1!.mDonutTime!.progressLabel.text = "0"
        mPlayer2!.mDonutTime!.progressLabel.text = "0"
        switch mQuestionType! {
        case 0:
            let controller1 = mPlayer1!.children.first! as! BlanksGameViewController
            let controller2 = mPlayer2!.children.first! as! BlanksGameViewController
            //            controller1.reveal()
            //            controller2.reveal()
            break
        case 1:
            let controller1 = mPlayer1!.children.first! as! DateGameViewController
            let controller2 = mPlayer2!.children.first! as! DateGameViewController
            //            controller1.reveal()
            //            controller2.reveal()
            break
        case 2:
            let controller1 = mPlayer1!.children.first! as! FlagGameViewController
            let controller2 = mPlayer2!.children.first! as! FlagGameViewController
            //            controller1.reveal()
            //            controller2.reveal()
            break
        case 3:
            let controller1 = mPlayer1!.children.first! as! DateGameViewController
            let controller2 = mPlayer2!.children.first! as! DateGameViewController
            //            controller1.reveal()
            //            controller2.reveal()
            break
        case 4:
            let controller1 = mPlayer1!.children.first! as! BlanksGameViewController
            let controller2 = mPlayer2!.children.first! as! BlanksGameViewController
            //            controller1.reveal()
            //            controller2.reveal()
            break
        default:
            break
        }
    }
    
    
    func showLoadingDialog() {
        let alert = UIAlertController(title: nil, message: NSLocalizedString("loading", comment: ""), preferredStyle: .alert)
        
        let loadingIndicator = UIActivityIndicatorView(frame: CGRect(x: 10, y: 5, width: 50, height: 50))
        loadingIndicator.hidesWhenStopped = true
        loadingIndicator.style = .medium
        loadingIndicator.startAnimating()
        
        alert.view.addSubview(loadingIndicator)
        present(alert, animated: false)
    }
    
    func closeLoadingDialog() {
        dismiss(animated: false)
    }
    
    func replaceFragment() {
        // if marks >= 380 then go to EndgameViewController return
        if mPointsA >= 380 {
                            if let view1 = mPlayer1!.mFragmentContainer.subviews.first {
                    showLoadingDialog()
                    view1.removeFromSuperview()
                    let controller = storyboard!.instantiateViewController(withIdentifier: "EndGameViewController") as! EndGameViewController
                    controller.view.frame = mPlayer1!.mFragmentContainer.bounds
                    controller.willMove(toParent: self)
                    mPlayer1!.mFragmentContainer.addSubview(controller.view)
                    mPlayer1!.addChild(controller)
                    closeLoadingDialog()
                    controller.didMove(toParent: self)
                    controller.view.backgroundColor = mPlayer1!.view.backgroundColor
                }
                if let view2 = mPlayer2!.mFragmentContainer.subviews.first {
                    showLoadingDialog()
                    view2.removeFromSuperview()
                    let controller = storyboard!.instantiateViewController(withIdentifier: "EndGameViewController") as! EndGameViewController
                    controller.view.frame = mPlayer2!.mFragmentContainer.bounds
                    controller.willMove(toParent: self)
                    mPlayer2!.mFragmentContainer.addSubview(controller.view)
                    mPlayer2!.addChild(controller)
                    closeLoadingDialog()
                    controller.didMove(toParent: self)
                    controller.view.backgroundColor = mPlayer2!.view.backgroundColor
                }
            performSegue(withIdentifier: "toHighscoreInputDialog", sender: nil)
            return
        } else if mPointsB >= 380 {
            if let view1 = mPlayer1!.mFragmentContainer.subviews.first {
                showLoadingDialog()
                view1.removeFromSuperview()
                let controller = storyboard!.instantiateViewController(withIdentifier: "EndGameViewController") as! EndGameViewController
                controller.view.frame = mPlayer1!.mFragmentContainer.bounds
                controller.willMove(toParent: self)
                mPlayer1!.mFragmentContainer.addSubview(controller.view)
                mPlayer1!.addChild(controller)
                closeLoadingDialog()
                controller.didMove(toParent: self)
                controller.view.backgroundColor = mPlayer1!.view.backgroundColor
            }
            if let view2 = mPlayer2!.mFragmentContainer.subviews.first {
                showLoadingDialog()
                view2.removeFromSuperview()
                let controller = storyboard!.instantiateViewController(withIdentifier: "EndGameViewController") as! EndGameViewController
                controller.view.frame = mPlayer2!.mFragmentContainer.bounds
                controller.willMove(toParent: self)
                mPlayer2!.mFragmentContainer.addSubview(controller.view)
                mPlayer2!.addChild(controller)
                closeLoadingDialog()
                controller.didMove(toParent: self)
                controller.view.backgroundColor = mPlayer2!.view.backgroundColor
            }
            performSegue(withIdentifier: "toHighscoreInputDialog", sender: nil)
            return
        }
        
        var x:Int = (0...4).randomElement()!
        while x == mQuestionType {
            x = (0...4).randomElement()!
        }
        switch x {
        case 0:
            if let view1 = mPlayer1!.mFragmentContainer.subviews.first {
                showLoadingDialog()
                view1.removeFromSuperview()
                let controller = storyboard!.instantiateViewController(withIdentifier: "BlanksGameViewController") as! BlanksGameViewController
                controller.view.frame = mPlayer1!.mFragmentContainer.bounds
                controller.willMove(toParent: self)
                mPlayer1!.mFragmentContainer.addSubview(controller.view)
                mPlayer1!.addChild(controller)
                closeLoadingDialog()
                controller.didMove(toParent: self)
                controller.view.backgroundColor = mPlayer1!.view.backgroundColor
            }
            if let view2 = mPlayer2!.mFragmentContainer.subviews.first {
                showLoadingDialog()
                view2.removeFromSuperview()
                let controller = storyboard!.instantiateViewController(withIdentifier: "BlanksGameViewController") as! BlanksGameViewController
                controller.view.frame = mPlayer2!.mFragmentContainer.bounds
                controller.willMove(toParent: self)
                mPlayer2!.mFragmentContainer.addSubview(controller.view)
                mPlayer2!.addChild(controller)
                closeLoadingDialog()
                controller.didMove(toParent: self)
                controller.view.backgroundColor = mPlayer2!.view.backgroundColor
            }
            mMaxTime = 60
            mSecs = 60
            mTimer = Timer.scheduledTimer(timeInterval: 1.0, target: self, selector: #selector(updateCounter), userInfo: nil, repeats: true)
        case 1:
            let sparql = """
            SELECT DISTINCT ?country ?countryLabel ?country_EN ?country_FR ?anthemLabel ?anthem_FR ?audioLabel
            WHERE
            {
            ?country wdt:P31 wd:Q3624078 .
            #not a former country
            FILTER NOT EXISTS {?country wdt:P31 wd:Q3024240}
            #and no an ancient civilisation (needed to exclude ancient Egypt)
            FILTER NOT EXISTS {?country wdt:P31 wd:Q28171280}
            ?country wdt:P85 ?anthem.
            ?anthem wdt:P51 ?audio.

            SERVICE wikibase:label { bd:serviceParam wikibase:language "en".
            }
            SERVICE wikibase:label { bd:serviceParam wikibase:language "en".
            ?country rdfs:label ?country_EN.
            } hint:Prior hint:runLast false.
            SERVICE wikibase:label { bd:serviceParam wikibase:language "fr".
            ?country rdfs:label ?country_FR.
            } hint:Prior hint:runLast false.
            SERVICE wikibase:label { bd:serviceParam wikibase:language "fr".
            ?anthem rdfs:label ?anthem_FR.
            } hint:Prior hint:runLast false.
            }
            ORDER BY ?countryLabel
            """
            AF.request("https://query.wikidata.org/sparql?format=json&query=\(sparql.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!)")
                .validate(statusCode: 200..<300)
                //            .validate(contentType: ["application/json"])
                .responseData { response in
                    switch response.result {
                    case .success(let value):
                        break
                    case .failure(let error):
                            break
                    }
            }
            if let view1 = mPlayer1!.mFragmentContainer.subviews.first {
                showLoadingDialog()
                view1.removeFromSuperview()
                let controller = storyboard!.instantiateViewController(withIdentifier: "DateGameViewController") as! DateGameViewController
                controller.view.frame = mPlayer1!.mFragmentContainer.bounds
                controller.willMove(toParent: self)
                mPlayer1!.mFragmentContainer.addSubview(controller.view)
                mPlayer1!.addChild(controller)
                closeLoadingDialog()
                controller.didMove(toParent: self)
                controller.view.backgroundColor = mPlayer1!.view.backgroundColor
            }
            if let view2 = mPlayer2!.mFragmentContainer.subviews.first {
                showLoadingDialog()
                view2.removeFromSuperview()
                let controller = storyboard!.instantiateViewController(withIdentifier: "DateGameViewController") as! DateGameViewController
                controller.view.frame = mPlayer2!.mFragmentContainer.bounds
                controller.willMove(toParent: self)
                mPlayer2!.mFragmentContainer.addSubview(controller.view)
                mPlayer2!.addChild(controller)
                closeLoadingDialog()
                controller.didMove(toParent: self)
                controller.view.backgroundColor = mPlayer2!.view.backgroundColor
            }
            mMaxTime = 10
            mSecs = 10
            mTimer = Timer.scheduledTimer(timeInterval: 1.0, target: self, selector: #selector(updateCounter), userInfo: nil, repeats: true)
        case 2:
            if let view1 = mPlayer1!.mFragmentContainer.subviews.first {
                showLoadingDialog()
                view1.removeFromSuperview()
                let controller = storyboard!.instantiateViewController(withIdentifier: "FlagGameViewController") as! FlagGameViewController
                controller.view.frame = mPlayer1!.mFragmentContainer.bounds
                controller.willMove(toParent: self)
                mPlayer1!.mFragmentContainer.addSubview(controller.view)
                mPlayer1!.addChild(controller)
                closeLoadingDialog()
                controller.didMove(toParent: self)
                controller.view.backgroundColor = mPlayer1!.view.backgroundColor
            }
            if let view2 = mPlayer2!.mFragmentContainer.subviews.first {
                showLoadingDialog()
                view2.removeFromSuperview()
                let controller = storyboard!.instantiateViewController(withIdentifier: "FlagGameViewController") as! FlagGameViewController
                controller.view.frame = mPlayer2!.mFragmentContainer.bounds
                controller.willMove(toParent: self)
                mPlayer2!.mFragmentContainer.addSubview(controller.view)
                mPlayer2!.addChild(controller)
                closeLoadingDialog()
                controller.didMove(toParent: self)
                controller.view.backgroundColor = mPlayer2!.view.backgroundColor
            }
            mMaxTime = 10
            mSecs = 10
            mTimer = Timer.scheduledTimer(timeInterval: 1.0, target: self, selector: #selector(updateCounter), userInfo: nil, repeats: true)
        case 3:
            if let view1 = mPlayer1!.mFragmentContainer.subviews.first {
                showLoadingDialog()
                view1.removeFromSuperview()
                let controller = storyboard!.instantiateViewController(withIdentifier: "MapGameViewController") as! MapGameViewController
                controller.view.frame = mPlayer1!.mFragmentContainer.bounds
                controller.willMove(toParent: self)
                mPlayer1!.mFragmentContainer.addSubview(controller.view)
                mPlayer1!.addChild(controller)
                closeLoadingDialog()
                controller.didMove(toParent: self)
                controller.view.backgroundColor = mPlayer1!.view.backgroundColor
            }
            if let view2 = mPlayer2!.mFragmentContainer.subviews.first {
                showLoadingDialog()
                view2.removeFromSuperview()
                let controller = storyboard!.instantiateViewController(withIdentifier: "MapGameViewController") as! MapGameViewController
                controller.view.frame = mPlayer2!.mFragmentContainer.bounds
                controller.willMove(toParent: self)
                mPlayer2!.mFragmentContainer.addSubview(controller.view)
                mPlayer2!.addChild(controller)
                closeLoadingDialog()
                controller.didMove(toParent: self)
                controller.view.backgroundColor = mPlayer2!.view.backgroundColor
            }
            mMaxTime = 10
            mSecs = 10
            mTimer = Timer.scheduledTimer(timeInterval: 1.0, target: self, selector: #selector(updateCounter), userInfo: nil, repeats: true)
        case 4:
            if let view1 = mPlayer1!.mFragmentContainer.subviews.first {
                showLoadingDialog()
                view1.removeFromSuperview()
                let controller = storyboard!.instantiateViewController(withIdentifier: "MapGameViewController") as! MapGameViewController
                controller.view.frame = mPlayer1!.mFragmentContainer.bounds
                controller.willMove(toParent: self)
                mPlayer1!.mFragmentContainer.addSubview(controller.view)
                mPlayer1!.addChild(controller)
                closeLoadingDialog()
                controller.didMove(toParent: self)
                controller.view.backgroundColor = mPlayer1!.view.backgroundColor
            }
            if let view2 = mPlayer2!.mFragmentContainer.subviews.first {
                showLoadingDialog()
                view2.removeFromSuperview()
                let controller = storyboard!.instantiateViewController(withIdentifier: "MapGameViewController") as! MapGameViewController
                controller.view.frame = mPlayer2!.mFragmentContainer.bounds
                controller.willMove(toParent: self)
                mPlayer2!.mFragmentContainer.addSubview(controller.view)
                mPlayer2!.addChild(controller)
                closeLoadingDialog()
                controller.didMove(toParent: self)
                controller.view.backgroundColor = mPlayer2!.view.backgroundColor
            }
            mMaxTime = 10
            mSecs = 10
            mTimer = Timer.scheduledTimer(timeInterval: 1.0, target: self, selector: #selector(updateCounter), userInfo: nil, repeats: true)
        default:
            break
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        mPlayer1Container.transform =  CGAffineTransform(rotationAngle: .pi)
        
        mPlayer1 = (children.first! as! ExteriorViewController)
        mPlayer2 = (children.last! as! ExteriorViewController)
        mPlayer1!.view.backgroundColor = UIColor(named: "CosmicLatte")!
        
        mMaxTime = 10
        mSecs = 10
        mTimer = Timer.scheduledTimer(timeInterval: 1.0, target: self, selector: #selector(updateCounter), userInfo: nil, repeats: true)
        
        mPlayer1!.mScoreChangeLbl!.text = "+0"
        mPlayer1!.mScoreChangeLbl!.textColor = UIColor(named: "AndroidGreen")!
        
        mPlayer2!.mScoreChangeLbl!.text = "-10"
        mPlayer2!.mScoreChangeLbl!.textColor = UIColor(named: "Firebrick")!
    }
}
